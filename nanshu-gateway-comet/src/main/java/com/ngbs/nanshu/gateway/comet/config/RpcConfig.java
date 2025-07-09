package com.ngbs.nanshu.gateway.comet.config;

import com.nanshu.grpc.nacos.EnableGrpcNacos;
import com.ngbs.nashu.gateway.logic.api.LogicConnectGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author 南顾北衫
 * @date 2024/8/24
 */
@Slf4j
@EnableGrpcNacos
@Configuration
public class RpcConfig {

    @Value("${grpc.logic-service}")
    private String logicServiceName;

    @Bean
    public LogicConnectGrpc.LogicConnectBlockingStub logicConnectGrpc() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget(logicServiceName).usePlaintext().build();
        return LogicConnectGrpc.newBlockingStub(managedChannel);
    }
}
