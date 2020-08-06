package com.redispubsub.demo.message;

import com.redispubsub.demo.constant.Constants;
import com.redispubsub.demo.pojo.Msg;
import com.redispubsub.demo.service.LocalCacheService;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;

import javax.annotation.Resource;

/**
 * 实现消息的接收功能
 * @author liuhongdi
 */
@Component
public class RedisMessageReceiver {

    @Resource
    private LocalCacheService localCacheService;

    //收到消息后进行处理
    public void onReceiveMessage(String message,String channel) {
        //System.out.println("message:"+message);
        message=message.replace("\\\"","\"");
        message=message.replace("\"{","{");
        message=message.replace("}\"","}");

        Msg msg = JSON.parseObject(message, Msg.class);
        System.out.println(channel+":消息来了："+msg.getMsgType()+";content:"+msg.getContent());

        if (channel.equals(Constants.CHANNEL_GOODS)) {

            if (msg.getMsgType().equals("deleteall")) {
                localCacheService.deleteGoodsCacheAll();
            } else if (msg.getMsgType().equals("delete") || msg.getMsgType().equals("update")) {
                String goodslist = msg.getContent();
                String[] strArr = goodslist.split(",");
                System.out.println(strArr);

                for (int i = 0; i < strArr.length; ++i){
                    Long goodsId = Long.parseLong(strArr[i]);
                    if (msg.getMsgType().equals("update")) {
                        localCacheService.updateGoodsCache(goodsId);
                    } else if (msg.getMsgType().equals("delete")) {
                        localCacheService.deleteGoodsCache(goodsId);
                    }
                }
            }

        }
    }
}