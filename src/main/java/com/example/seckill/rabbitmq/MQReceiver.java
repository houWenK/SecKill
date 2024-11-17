package com.example.seckill.rabbitmq;


import com.example.seckill.dto.GoodsDto;
import com.example.seckill.pojo.SeckillMessage;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.service.GoodsService;
import com.example.seckill.service.OrderService;
import com.example.seckill.service.SeckillGoodsService;
import com.example.seckill.service.SeckillOrderService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue()
                .get("order:" + seckillMessage.getUser().getId() + ":" + seckillMessage.getGoodsId());
        if (seckillOrder != null) {
            return ;
        }
        GoodsDto goodsDto = goodsService.fintGoodsDtoById(seckillMessage.getGoodsId());
        if(goodsDto.getStockCount()<1){
            return;
        }
        orderService.seckill(seckillMessage.getUser(),goodsDto);
    }
}
