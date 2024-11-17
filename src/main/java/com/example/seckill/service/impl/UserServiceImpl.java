package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.seckill.dto.LoginDto;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.pojo.User;
import com.example.seckill.mapper.UserMapper;
import com.example.seckill.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.utils.CookieUtil;
import com.example.seckill.utils.MD5Util;
import com.example.seckill.utils.UUIDUtil;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author houwenke
 * @since 2024-11-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginDto loginVo, HttpServletRequest request, HttpServletResponse response) {
        String id= loginVo.getId();
        String password= loginVo.getPassword();
//        if(StringUtils.isEmpty(password)||StringUtils.isEmpty(id)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isMobile(id)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        User user=userMapper.selectById(id);
        if (user==null){
//            return  RespBean.error(RespBeanEnum.MOBILE_NOT_EXIST);
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        if(!StringUtils.equals(user.getPassword(), MD5Util.formPassToDBPass(password,user.getSalt()))){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        String ticket=UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket,user);
//        request.getSession().setAttribute(ticket,user);//session
        CookieUtil.setCookie(request,response,"userTicket",ticket);//cookie
        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    @Override
    public RespBean updatePassword(String userTicket, Long id, String password, HttpServletRequest request, HttpServletResponse response) {
        User user=getUserByCookie(userTicket,request,response);
        if (user==null){
            throw new GlobalException(RespBeanEnum.SESSION_ERROR);
        }
        user.setPassword(MD5Util.formPassToDBPass(password,user.getSalt()));
        int result=userMapper.updateById(user);
        if(result==1){
            redisTemplate.delete("user"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
