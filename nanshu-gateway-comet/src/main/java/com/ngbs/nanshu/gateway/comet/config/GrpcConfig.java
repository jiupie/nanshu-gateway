package com.ngbs.nanshu.gateway.comet.config;

import com.nanshu.grpc.nacos.properties.GrpcProperties;
import com.ngbs.nanshu.gateway.comet.rpc.CometMsgImpl;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GrpcConfig implements CommandLineRunner {
    @Autowired
    private GrpcProperties grpcProperties;

    @Override
    public void run(String... args) {
        try {
            ServerBuilder.forPort(grpcProperties.getPort()).addService(new CometMsgImpl()).build().start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
