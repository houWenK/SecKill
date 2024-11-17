package com.example.seckill.controller;


import com.example.seckill.dto.DetailDto;
import com.example.seckill.dto.GoodsDto;
import com.example.seckill.pojo.User;
import com.example.seckill.service.GoodsService;
import com.example.seckill.service.UserService;
import com.example.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/tolist")
//    public RespBean toList(@CookieValue("userTicket") String ticket, HttpServletRequest request, HttpServletResponse response){
    public RespBean toList(){
//            if(StringUtils.isEmpty(ticket)){
//                throw new GlobalException(RespBeanEnum.SESSION_ERROR);
//            }
//            User user=(User) session.getAttribute(ticket);
//            User user=userService.getUserByCookie(ticket,request,response);
            List<GoodsDto> goodsDto=goodsService.findGoodsDto();
            return RespBean.success(goodsDto);
    }

    @RequestMapping("/todetail/{goodsid}")
    public RespBean toDetail(User user,@PathVariable(name="goodsid") Long goosId){
        GoodsDto goodsDto=goodsService.fintGoodsDtoById(goosId);
        Date startDate=goodsDto.getStartDate();
        Date endDate=goodsDto.getEndDate();
        Date nowDate=new Date();
        int secKillStatus=0;
        int remainSeconds=0;
        if(nowDate.before(startDate)){
            remainSeconds=((int)((startDate.getTime()-nowDate.getTime())/1000));
        }else if(nowDate.after(endDate)){
            secKillStatus=2;
            remainSeconds=-1;
        }else{
            secKillStatus=1;
            remainSeconds=0;
        }
        DetailDto detailDto = new DetailDto();
        detailDto.setUser(user);
        detailDto.setGoodsDto(goodsDto);
        detailDto.setSecKillStatus(secKillStatus);
        detailDto.setRemainSeconds(remainSeconds);
        return RespBean.success(detailDto);
    }

}
