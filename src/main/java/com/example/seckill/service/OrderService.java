package com.example.seckill.service;

import com.example.seckill.dto.GoodsDto;
import com.example.seckill.dto.OrderDetailDto;
import com.example.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houwenke
 * @since 2024-11-10
 */
public interface OrderService extends IService<Order> {


    Order seckill(User user, GoodsDto goodsDto);

    OrderDetailDto detail(Long orderId);

    String createPath(User user, Long goodsId);

    boolean checkCaptcha(User user, Long goodsId, String captcha);

    boolean checkPath(User user, Long goodsId, String path);
}
