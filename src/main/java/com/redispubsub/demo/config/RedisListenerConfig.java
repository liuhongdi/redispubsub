package com.redispubsub.demo.config;

import com.redispubsub.demo.constant.Constants;
import com.redispubsub.demo.message.RedisMessageReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * redis 的listener配置
    by liuhongdi
 */
@Configuration
public class RedisListenerConfig {

    //创建两个消息监听器MessageListener
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(Constants.CHANNEL_GOODS));
        container.addMessageListener(listenerAdapter, new PatternTopic(Constants.CHANNEL_HOME));
        return container;
    }

    //指定接收消息的类名和方法名
    @Bean
    MessageListenerAdapter listenerAdapter(RedisMessageReceiver messageReceiver) {
        System.out.println("listenerAdapter");
        return new MessageListenerAdapter(messageReceiver, "onReceiveMessage");
    }

    //指定StringRedisTemplate的生成
    @Bean
    StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}