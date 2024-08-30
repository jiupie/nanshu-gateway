package com.ngbs.nanshu.gateway.logic.rpc;

import com.ngbs.nashu.gateway.logic.api.GatewayConnect;
import com.ngbs.nashu.gateway.logic.api.model.ConnectReq;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author 南顾北衫
 * @date 2024/8/24
 */
@DubboService
public class GatewayConnectImpl implements GatewayConnect {

    @Override
    public void connect(ConnectReq connectReq) {

    }
}
