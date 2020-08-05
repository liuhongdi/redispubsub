package com.redispubsub.demo.service;

import com.redispubsub.demo.pojo.Goods;

public interface LocalCacheService {
    public Goods updateGoodsCache(Long goodsId);
    public void  deleteGoodsCache(Long goodsId);
}
