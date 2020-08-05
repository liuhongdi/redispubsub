package com.redispubsub.demo.service;

import com.redispubsub.demo.pojo.Goods;

public interface GoodsService {
    public Goods getOneGoodsById(Long goodsId);
}