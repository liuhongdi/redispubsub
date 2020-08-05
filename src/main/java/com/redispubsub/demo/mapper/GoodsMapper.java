package com.redispubsub.demo.mapper;

import com.redispubsub.demo.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface GoodsMapper {
    Goods selectOneGoods(Long goodsId);
}
