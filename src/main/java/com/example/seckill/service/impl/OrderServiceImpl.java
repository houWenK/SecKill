package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.seckill.dto.GoodsDto;
import com.example.seckill.dto.OrderDetailDto;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.mapper.SeckillOrderMapper;
import com.example.seckill.pojo.Order;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.service.GoodsService;
import com.example.seckill.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.service.SeckillGoodsService;
import com.example.seckill.service.SeckillOrderService;
import com.example.seckill.utils.MD5Util;
import com.example.seckill.utils.UUIDUtil;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author houwenke
 * @since 2024-11-10
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
//    @Autowired
//    private SeckillOrderMapper seckillOrderMapper;

    @Transactional
    @Override
    public Order seckill(User user, GoodsDto goodsDto) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                .eq("goods_id", goodsDto.getId()));
//        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
//                .set("stock_count", seckillGoods.getStockCount())
//                .eq("id", seckillGoods.getGoodsId())
//                .gt("stock_count", 0));
        boolean update = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count=" + "stock_count-1")
                .eq("goods_id", goodsDto.getId())
                .gt("stock_count", 0));
        if(seckillGoods.getStockCount()<1){
            redisTemplate.opsForValue().set("isStockEmpty:"+goodsDto.getId(),"0");
            return null;
        }
//        if(!update){
//            return null;
//        }
        Order order = new Order();
        order.setGoodsId(seckillGoods.getGoodsId());
        order.setUserId(user.getId());
        order.setOrderChannel(1);
        order.setGoodsCount(1);
        order.setStatus(0);
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsDto.getGoodsName());
        order.setCreateDate(LocalDateTime.now());
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        orderMapper.insert(order);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsDto.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goodsDto.getId(),
                seckillOrder);
        return order;
    }

    @Override
    public OrderDetailDto detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsDto goodsDto = goodsService.fintGoodsDtoById(order.getGoodsId());
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setOrder(order);
        orderDetailDto.setGoodsDto(goodsDto);
        return orderDetailDto;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 1, TimeUnit.MINUTES);
        return str;
    }

    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(captcha)) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }
}
