package com.ngbs.nanshu.gateway.logic.config;

import com.nanshu.grpc.nacos.properties.GrpcProperties;
import com.ngbs.nanshu.gateway.logic.rpc.LogicConnectGrpcImpl;
import com.ngbs.nanshu.gateway.logic.utils.IdGenerator;
import com.ngbs.nanshu.gateway.logic.utils.RedisService;
import io.grpc.ServerBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class GrpcConfig implements CommandLineRunner {
    @Resource
    private GrpcProperties grpcProperties;

    @Resource
    private RedisService redisService;

    @Resource
    private IdGenerator idGenerator;

    @Override
    public void run(String... args) {
        try {
            ServerBuilder.forPort(grpcProperties.getPort())
                    .addService(new LogicConnectGrpcImpl(idGenerator, redisService))
                    .build()
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
