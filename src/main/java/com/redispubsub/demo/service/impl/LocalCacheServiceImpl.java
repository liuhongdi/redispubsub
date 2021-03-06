package com.redispubsub.demo.service.impl;

import com.redispubsub.demo.pojo.Goods;
import com.redispubsub.demo.service.LocalCacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LocalCacheServiceImpl implements LocalCacheService {

    @Resource
    private RedisTemplate redis1Template;

    //更新缓存
    @CachePut(value = "goods", key="#goodsId")
    @Override
    public Goods updateGoodsCache(Long goodsId){
        System.out.println("get data from redis");
        Goods goodsr = (Goods) redis1Template.opsForValue().get("goods_"+String.valueOf(goodsId));
        return goodsr;
    }

    //删除缓存
    @CacheEvict(value = "goods" ,key = "#goodsId")
    @Override
    public void deleteGoodsCache(Long goodsId) {
        System.out.println("删除缓存 ");
    }

    //清除缓存
    @CacheEvict(value = "goods", allEntries=true)
    @Override
    public void deleteGoodsCacheAll() {
        System.out.println("已删除全部缓存 ");
    }
}
