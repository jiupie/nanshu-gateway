package com.ngbs.nanshu.gateway.comet.server;

import com.ngbs.nanshu.gateway.comet.application.producer.ChannelMessageProducer;
import com.ngbs.nanshu.gateway.comet.config.SpringContextHolder;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author 南顾北衫
 */
@ChannelHandler.Sharable
public class NettyConnectManageHandler extends ChannelDuplexHandler {
    private static final Logger log = LoggerFactory.getLogger(NettyConnectManageHandler.class);


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String channelId = RemotingUtil.getChannelId(ctx.channel());
        if (StringUtils.hasText(channelId)) {
            ChannelMessageProducer channelMessageProducer = SpringContextHolder.getBean(ChannelMessageProducer.class);
            channelMessageProducer.sendOfflineMsg(channelId);
        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.ALL_IDLE)) {
                RemotingUtil.closeChannel(ctx.channel());
            }
        }

        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            RemotingUtil.addChannel(ctx.channel());
        }
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("NETTY SERVER PIPELINE: exceptionCaught exception.", cause);
        RemotingUtil.closeChannel(ctx.channel());
    }
}
