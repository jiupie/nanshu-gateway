package com.ngbs.nanshu.gateway.comet.server.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngbs.gateway.common.model.req.GatewayBaseReq;
import com.ngbs.nanshu.gateway.comet.config.SpringContextHolder;
import com.ngbs.nashu.gateway.logic.api.LogicConnectGrpc;
import com.ngbs.nashu.gateway.logic.api.LogicConnectService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;

import java.util.List;

import static com.ngbs.gateway.common.model.req.GatewayRequestConstant.HEARTBEAT;
import static com.ngbs.nanshu.gateway.comet.server.RemotingUtil.ID_ATTR;

/**
 * @author 南顾北衫
 * @date 2024/8/10
 */
public class NanshuWebSocketTextHandler extends MessageToMessageCodec<TextWebSocketFrame, TextWebSocketFrame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        msg.retain();
        out.add(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);
        GatewayBaseReq gatewayBaseReq = objectMapper.readValue(msg.text(), GatewayBaseReq.class);
        if (ctx.channel().hasAttr(ID_ATTR)) {
            Attribute<String> attr = ctx.channel().attr(ID_ATTR);
            gatewayBaseReq.setChannelId(attr.get());
        } else {
            ctx.channel().close();
            return;
        }
        String type = gatewayBaseReq.getType();
        if (type.equals(HEARTBEAT)) {
            LogicConnectService.HeartbeatRequest heartbeatRequest = LogicConnectService.HeartbeatRequest.newBuilder().setKey(gatewayBaseReq.getChannelId()).build();
            SpringContextHolder.getBean(LogicConnectGrpc.LogicConnectBlockingStub.class).heartBeat(heartbeatRequest);
        }

    }
}
