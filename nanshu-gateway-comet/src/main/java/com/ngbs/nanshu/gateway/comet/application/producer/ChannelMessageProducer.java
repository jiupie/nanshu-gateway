package com.ngbs.nanshu.gateway.comet.application.producer;


import com.ngbs.gateway.common.model.req.GatewayBaseReq;
import com.ngbs.gateway.common.model.req.GatewayOfflineReq;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
@Component
public class ChannelMessageProducer {
    private static final String CHANNEL_MESSAGE_TOPIC = "channelMsg";
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void channelMsgTopic(GatewayBaseReq msg) {
        rocketMQTemplate.convertAndSend(CHANNEL_MESSAGE_TOPIC, msg);
    }

    public void sendOfflineMsg(String channelId) {
        GatewayOfflineReq gatewayOfflineReq = new GatewayOfflineReq();
        gatewayOfflineReq.setChannelId(channelId);
        this.channelMsgTopic(gatewayOfflineReq);
    }
}
