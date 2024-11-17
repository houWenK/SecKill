package com.example.seckill.controller;


import com.example.seckill.pojo.User;
import com.example.seckill.utils.CookieUtil;
import com.example.seckill.vo.RespBean;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author houwenke
 * @since 2024-11-09
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/info")
    public RespBean inFo(User user){
        return RespBean.success(user);
    }




}
