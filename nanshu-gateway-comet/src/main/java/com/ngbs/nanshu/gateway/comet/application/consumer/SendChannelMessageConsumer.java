package com.ngbs.nanshu.gateway.comet.application.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngbs.gateway.common.model.dto.GatewayDTO;
import com.ngbs.nanshu.gateway.comet.server.RemotingUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
@RocketMQMessageListener(consumerGroup = "gatewayMsgConsumer", topic = "sendChannelMsg")
@Component
public class SendChannelMessageConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(MessageExt message) {
        try {
            GatewayDTO gatewayDTO = objectMapper.readValue(message.getBody(), GatewayDTO.class);
            List<String> channelIds = gatewayDTO.getChannelIds();
            for (String channelId : channelIds) {
                Channel channel = RemotingUtil.getChannel(channelId);
                if (channel == null || gatewayDTO.getData() == null) {
                    continue;
                }
                String msgData = objectMapper.writeValueAsString(gatewayDTO.getData());
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msgData);
                channel.writeAndFlush(textWebSocketFrame);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
