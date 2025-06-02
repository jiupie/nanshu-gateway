package com.ngbs.nanshu.gateway.job.application.consumer;

import com.ngbs.gateway.common.constants.NanshuGatewayConstants;
import com.ngbs.gateway.common.model.dto.GatewayDTO;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(consumerGroup = "msgChannelGroup", topic = NanshuGatewayConstants.SEND_CHANNEL_MESSAGE_TOPIC)
@Component
public class ChannelMsgConsumer implements RocketMQListener<GatewayDTO> {

    @Override
    public void onMessage(GatewayDTO gatewayDTO) {

    }
}
