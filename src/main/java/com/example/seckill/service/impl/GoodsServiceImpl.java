package com.example.seckill.service.impl;

import com.example.seckill.dto.GoodsDto;
import com.example.seckill.mapper.SeckillGoodsMapper;
import com.example.seckill.pojo.Goods;
import com.example.seckill.mapper.GoodsMapper;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author houwenke
 * @since 2024-11-10
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Autowired
    private  GoodsMapper goodsMapper;

    @Override
    public List<GoodsDto> findGoodsDto() {
        return goodsMapper.findGoodsDto();
    }

    @Override
    public GoodsDto fintGoodsDtoById(Long goosId) {
        return goodsMapper.findGoodsDtoById(goosId);
    }
}
