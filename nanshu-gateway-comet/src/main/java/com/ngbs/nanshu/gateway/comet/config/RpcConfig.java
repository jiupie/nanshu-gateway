package com.ngbs.nanshu.gateway.comet.config;

import com.ngbs.nashu.gateway.logic.api.GatewayConnect;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author 南顾北衫
 * @date 2024/8/24
 */
@Component
public class RpcConfig {

    @DubboReference
    private GatewayConnect gatewayConnect;

    public GatewayConnect getGatewayConnect() {
        return gatewayConnect;
    }
}
