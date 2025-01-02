package com.ngbs.nanshu.gateway.comet.server;

import com.ngbs.nanshu.gateway.comet.config.SpringContextHolder;
import com.ngbs.nashu.gateway.logic.api.LogicConnectGrpc;
import com.ngbs.nashu.gateway.logic.api.LogicConnectService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author 南顾北衫
 * @date 2024/8/10
 */
public class AuthHandler extends MessageToMessageDecoder<FullHttpRequest> {

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        String uri = msg.uri();
        Map<String, String> map = RemotingUtil.parseQuery(uri);
        String token = map.get("token");

        NettyServer bean = SpringContextHolder.getBean(NettyServer.class);
        LogicConnectService.ConnectReq connectReq = LogicConnectService.ConnectReq.newBuilder()
                .setToken(token)
                .setServerId(bean.server()).build();
        LogicConnectService.ConnectResponse connectResponse = SpringContextHolder.getBean(LogicConnectGrpc.LogicConnectBlockingStub.class).connect(connectReq);
        if (StringUtils.hasText(connectResponse.getUserId())) {
            RemotingUtil.addChannel(ctx.channel(), connectResponse.getKey());
            msg.retain();
            out.add(msg);
            return;
        }
        HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
        ctx.writeAndFlush(res);
    }
}
