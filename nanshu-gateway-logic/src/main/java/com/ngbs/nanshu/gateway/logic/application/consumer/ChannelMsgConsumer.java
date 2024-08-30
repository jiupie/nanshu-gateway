package com.ngbs.nanshu.gateway.logic.application.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngbs.gateway.common.model.req.GatewayBaseReq;
import com.ngbs.gateway.common.model.req.GatewayConnectReq;
import com.ngbs.gateway.common.model.req.GatewayOfflineReq;
import com.ngbs.gateway.common.model.vo.GatewayVO;
import com.ngbs.nanshu.gateway.logic.application.producer.ChannelSendMessageProducer;
import com.ngbs.nanshu.gateway.logic.constant.RedisKey;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
@RocketMQMessageListener(consumerGroup = "gatewayApiConsumerGroup", topic = "channelMsg")
@Component
public class ChannelMsgConsumer implements RocketMQListener<MessageExt> {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ChannelSendMessageProducer channelSendMessageProducer;


    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        try {
            GatewayBaseReq gatewayBaseReq = objectMapper.readValue(body, GatewayBaseReq.class);
            if (gatewayBaseReq instanceof GatewayConnectReq) {
                GatewayConnectReq gatewayConnectReq = (GatewayConnectReq) gatewayBaseReq;
                Set<String> ids = redisTemplate.opsForZSet().range(RedisKey.fileConnectKey(gatewayConnectReq.getRouteType(), gatewayConnectReq.getRouteKey()), 0, -1);
                redisTemplate.opsForZSet().add(RedisKey.userConnectKey(gatewayConnectReq.getUserId().toString()), gatewayConnectReq.getChannelId(), System.currentTimeMillis() / 1000);
                redisTemplate.opsForZSet().add(RedisKey.fileConnectKey(gatewayConnectReq.getRouteType(), gatewayConnectReq.getRouteKey()), gatewayConnectReq.getChannelId(), System.currentTimeMillis() / 1000);
                redisTemplate.opsForHash().put(RedisKey.connectHashKey(gatewayConnectReq.getChannelId()), gatewayConnectReq.getUserId().toString(), RedisKey.fileConnectStr(gatewayConnectReq.getRouteType(), gatewayConnectReq.getRouteKey()));

                if (ids.isEmpty()) {
                    return;
                }
                GatewayVO<String> stringGatewayVO = new GatewayVO<>();
                stringGatewayVO.setChannelIds(new ArrayList<>(ids));
                stringGatewayVO.setData(gatewayConnectReq.getUsername() + "[" + gatewayConnectReq.getUserId() + "]上线了!!");
                channelSendMessageProducer.sendChannelMsgTopic(stringGatewayVO);
            } else if (gatewayBaseReq instanceof GatewayOfflineReq) {
                Map<String, String> entries = redisTemplate.opsForHash().entries(RedisKey.connectHashKey(gatewayBaseReq.getChannelId()));
                entries.forEach((k, v) -> {
                    redisTemplate.opsForZSet().remove(RedisKey.userConnectKey(k), gatewayBaseReq.getChannelId());
                    redisTemplate.opsForZSet().remove(RedisKey.fileConnectKey(v), gatewayBaseReq.getChannelId());
                    Set<String> ids = redisTemplate.opsForZSet().range(RedisKey.fileConnectKey(v), 0, -1);
                    if (!ids.isEmpty()) {
                        GatewayVO<String> stringGatewayVO = new GatewayVO<>();
                        stringGatewayVO.setChannelIds(new ArrayList<>(ids));
                        stringGatewayVO.setData("[" + k + "]下线了!!");
                        channelSendMessageProducer.sendChannelMsgTopic(stringGatewayVO);
                    }
                });
                redisTemplate.delete(RedisKey.connectHashKey(gatewayBaseReq.getChannelId()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
