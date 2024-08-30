package com.ngbs.nanshu.gateway.comet.config;

import com.ngbs.nanshu.gateway.comet.server.NettyServer;
import com.ngbs.nanshu.gateway.comet.server.NettyServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 南顾北衫
 * @date 2024/8/9
 */
@Configuration
public class ServerConfig {

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public NettyServer nettyServer() {
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        return new NettyServer(nettyServerConfig);
    }
}
