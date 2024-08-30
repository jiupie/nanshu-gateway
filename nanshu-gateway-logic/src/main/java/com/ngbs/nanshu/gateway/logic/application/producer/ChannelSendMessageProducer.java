package com.ngbs.nanshu.gateway.logic.application.producer;

import com.ngbs.gateway.common.model.vo.GatewayVO;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
@Component
public class ChannelSendMessageProducer {
    private static final String SEND_CHANNEL_MESSAGE_TOPIC = "sendChannelMsg";
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendChannelMsgTopic(GatewayVO msg) {
        rocketMQTemplate.convertAndSend(SEND_CHANNEL_MESSAGE_TOPIC, msg);
    }

}
