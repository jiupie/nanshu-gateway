package com.ngbs.nanshu.gateway.logic.application.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
@RocketMQMessageListener(consumerGroup = "gatewayApiConsumerGroup", topic = "channelMsg")
@Component
public class ChannelMsgConsumer implements RocketMQListener<MessageExt> {


    @Override
    public void onMessage(MessageExt message) {

    }
}
