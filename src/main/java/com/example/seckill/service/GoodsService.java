package com.example.seckill.service;

import com.example.seckill.dto.GoodsDto;
import com.example.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author houwenke
 * @since 2024-11-10
 */
public interface GoodsService extends IService<Goods> {

    List<GoodsDto> findGoodsDto();

    GoodsDto fintGoodsDtoById(Long goosId);
}
