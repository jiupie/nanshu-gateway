package com.ngbs.nanshu.gateway.comet.config;

import com.ngbs.nashu.gateway.logic.api.LogicConnectGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 南顾北衫
 * @date 2024/8/24
 */
@Component
public class RpcConfig {
    @Bean
    public ManagedChannel getGatewayConnect() {
        String target = "127.0.0.1:10233";
        return ManagedChannelBuilder.forTarget(target).usePlaintext().build();
    }

    @Bean
    public LogicConnectGrpc.LogicConnectBlockingStub logicConnectGrpc(ManagedChannel managedChannel) {
        return LogicConnectGrpc.newBlockingStub(managedChannel);
    }
}
