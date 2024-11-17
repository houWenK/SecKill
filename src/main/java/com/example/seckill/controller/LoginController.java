package com.example.seckill.controller;


import com.example.seckill.dto.LoginDto;
import com.example.seckill.service.UserService;
import com.example.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/tologin",method = RequestMethod.POST)
    public RespBean toLogin(@RequestBody @Valid LoginDto loginVo, HttpServletRequest request, HttpServletResponse response){
        return  userService.doLogin(loginVo,request,response);
    }
}
