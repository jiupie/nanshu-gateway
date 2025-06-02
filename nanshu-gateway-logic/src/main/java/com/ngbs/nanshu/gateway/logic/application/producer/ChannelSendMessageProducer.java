package com.ngbs.nanshu.gateway.logic.application.producer;

import com.ngbs.gateway.common.constants.NanshuGatewayConstants;
import com.ngbs.gateway.common.model.dto.GatewayDTO;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
@Component
public class ChannelSendMessageProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendChannelMsgTopic(GatewayDTO msg) {
        rocketMQTemplate.convertAndSend(NanshuGatewayConstants.SEND_CHANNEL_MESSAGE_TOPIC, msg);
    }

}
