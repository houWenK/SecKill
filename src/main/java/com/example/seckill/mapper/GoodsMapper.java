package com.example.seckill.mapper;

import com.example.seckill.dto.GoodsDto;
import com.example.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author houwenke
 * @since 2024-11-10
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    @Select("SELECT g.id, " +
            "g.goods_name, " +
            "g.goods_title, " +
            "g.goods_img, " +
            "g.goods_price, " +
            "g.goods_stock, " +
            "sg.seckill_price, " +
            "sg.stock_count, " +
            "sg.start_date, " +
            "sg.end_date " +
            "FROM t_goods g " +
            "INNER JOIN t_seckill_goods sg ON g.id = sg.goods_id")
    List<GoodsDto> findGoodsDto();

    @Select("SELECT g.id, " +
            "g.goods_name, " +
            "g.goods_title, " +
            "g.goods_img, " +
            "g.goods_price, " +
            "g.goods_stock, " +
            "sg.seckill_price, " +
            "sg.stock_count, " +
            "sg.start_date, " +
            "sg.end_date " +
            "FROM t_goods g " +
            "INNER JOIN t_seckill_goods sg ON g.id = sg.goods_id " +
            "where g.id=#{goodsId}")
    GoodsDto findGoodsDtoById(Long goodsId);
}
