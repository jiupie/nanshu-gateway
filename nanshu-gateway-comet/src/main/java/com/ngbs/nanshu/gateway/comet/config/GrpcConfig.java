package com.ngbs.nanshu.gateway.comet.config;

import com.ngbs.nanshu.gateway.comet.rpc.CometMsgImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GrpcConfig implements ApplicationContextAware, CommandLineRunner {

    @Value("${grpc.port}")
    private int grpcPort;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        Runnable runnable = () -> {
            Server server = null;
            try {
                server = ServerBuilder.forPort(grpcPort).addService(new CometMsgImpl()).build().start();
                server.awaitTermination();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(runnable, "grpc-server-" + grpcPort).start();
    }
}
