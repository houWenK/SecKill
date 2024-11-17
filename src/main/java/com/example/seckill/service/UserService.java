package com.example.seckill.service;

import com.example.seckill.dto.LoginDto;
import com.example.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author houwenke
 * @since 2024-11-09
 */
public interface UserService extends IService<User> {

    RespBean doLogin(LoginDto loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    RespBean updatePassword(String userTicket,Long id,String password,HttpServletRequest request,HttpServletResponse response);
}
