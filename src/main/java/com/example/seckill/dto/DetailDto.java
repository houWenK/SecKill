package com.example.seckill.dto;


import com.example.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDto {
    private User user;
    private GoodsDto goodsDto;
    private int secKillStatus;
    private int remainSeconds;
}
