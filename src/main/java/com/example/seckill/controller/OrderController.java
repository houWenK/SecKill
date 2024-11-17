package com.example.seckill.controller;


import com.example.seckill.dto.OrderDetailDto;
import com.example.seckill.pojo.User;
import com.example.seckill.service.OrderService;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author houwenke
 * @since 2024-11-10
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/detail")
    public RespBean detail(User user,Long orderId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailDto orderDetailDto=orderService.detail(orderId);
        return RespBean.success(orderDetailDto);
    }

}
