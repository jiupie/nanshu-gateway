package com.ngbs.nanshu.gateway.comet.config;

import com.ngbs.nanshu.gateway.comet.server.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * @author 南顾北衫
 * @date 2024/8/25
 */
@Component
public class ServerStarterLifecycle implements SmartLifecycle, ApplicationContextAware {
    private NettyServer nettyServer;
    private ApplicationContext applicationContext;

    private volatile boolean running;

    @Override
    public void start() {
        this.nettyServer = applicationContext.getBean(NettyServer.class);
        new Thread(() -> {
            nettyServer.start();
        }, "nettyServer").start();
        this.running = true;
    }

    @Override
    public void stop() {
        if (nettyServer != null) {
            nettyServer.shutdown();
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
