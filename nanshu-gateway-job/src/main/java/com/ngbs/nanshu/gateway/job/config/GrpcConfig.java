package com.ngbs.nanshu.gateway.job.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

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
        IdGenerator idGenerator = this.applicationContext.getBean(IdGenerator.class);
        Server server = null;
        try {
            server = ServerBuilder.forPort(grpcPort)
//                    .addService(new LogicConnectGrpcImpl(idGenerator, redisService))
                    .build()
                    .start();
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
