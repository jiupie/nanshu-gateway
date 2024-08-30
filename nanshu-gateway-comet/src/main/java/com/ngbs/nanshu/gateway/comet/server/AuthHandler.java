package com.ngbs.nanshu.gateway.comet.server;

import com.ngbs.nanshu.gateway.comet.config.RpcConfig;
import com.ngbs.nanshu.gateway.comet.config.SpringContextHolder;
import com.ngbs.nashu.gateway.logic.api.GatewayConnect;
import com.ngbs.nashu.gateway.logic.api.model.ConnectReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.*;

import java.util.List;
import java.util.Map;

/**
 * @author 南顾北衫
 * @date 2024/8/10
 */
public class AuthHandler extends MessageToMessageDecoder<FullHttpRequest> {
    private static RpcConfig rpcConfig = null;

    static {
        rpcConfig = SpringContextHolder.getBean(RpcConfig.class);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        String uri = msg.uri();
        Map<String, String> map = RemotingUtil.parseQuery(uri);
        String token = map.get("token");
        ConnectReq connectReq = new ConnectReq();
        connectReq.setToken(token);
        NettyServer bean = SpringContextHolder.getBean(NettyServer.class);
        bean.server();
//        connectReq.setServer();
        rpcConfig.getGatewayConnect().connect(connectReq);
        if (token != null) {
            msg.retain();
            out.add(msg);
            return;
        }
        HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
        ctx.writeAndFlush(res);

    }
}
