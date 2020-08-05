package com.redispubsub.demo.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.redispubsub.demo.pojo.Goods;
import com.redispubsub.demo.pojo.Msg;
import com.redispubsub.demo.service.GoodsService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/home")
public class HomeController {

    //@Resource
    //private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redis1Template;

    @Resource
    private GoodsService goodsService;

    @Resource
    private CacheManager getCacheManager;

    @GetMapping("/update")
    public String update(){

           String ret = "";

           int goodsId = 3;
            //更新redis
           System.out.println("get data from redis");
           String key = "goods_"+String.valueOf(goodsId);
           Goods goodsr = (Goods)redis1Template.opsForValue().get(key);
           ret = "更新前:<br/>"+goodsr.toString()+"<br/>";
           String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(System.currentTimeMillis());
           goodsr.setGoodsName("更新后的商品名，更新时间:"+now);

           redis1Template.opsForValue().set(key,goodsr);

           Goods goodsr2 = (Goods)redis1Template.opsForValue().get(key);
           ret += "更新后:<br/>"+goodsr2.toString()+"<br/>";

            //发布消息，接收者更新本地cache
            Msg msg_up = new Msg();
            msg_up.setMsgType("update");
            msg_up.setContent("3,5");
        redis1Template.convertAndSend("goodsCache",JSON.toJSONString(msg_up));

        //删除id为4的商品的缓存
            Msg msg_del = new Msg();
            msg_del.setMsgType("delete");
            msg_del.setContent("4");
        redis1Template.convertAndSend("goodsCache",JSON.toJSONString(msg_del));

        return ret;
    }


    //商品详情 参数:商品id
    @Cacheable(value = "goods", key="#goodsId",sync = true)
    @GetMapping("/goodsget")
    @ResponseBody
    public Goods goodsInfo(@RequestParam(value="goodsid",required = true,defaultValue = "0") Long goodsId) {
        Goods goods = goodsService.getOneGoodsById(goodsId);
        return goods;
    }

    //统计，如果是生产环境，需要加密才允许访问
    @GetMapping("/stats")
    @ResponseBody
    public Object stats() {

        CaffeineCache caffeine = (CaffeineCache)getCacheManager.getCache("goods");
        Cache goods = caffeine.getNativeCache();
        String statsInfo="cache名字:goods<br/>";
        Long size = goods.estimatedSize();
        statsInfo += "size:"+size+"<br/>";
        ConcurrentMap map= goods.asMap();
        statsInfo += "map keys:<br/>";
        for(Object key : map.keySet()) {
            statsInfo += "key:"+key.toString()+";value:"+map.get(key)+"<br/>";
        }
        statsInfo += "统计信息:"+goods.stats().toString();

        return statsInfo;
    }


}
