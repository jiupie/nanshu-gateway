package com.ngbs.nanshu.gateway.logic;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
@SpringBootApplication
public class GatewayLogicApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .web(WebApplicationType.NONE)
                .sources(GatewayLogicApplication.class)
                .run(args);
    }
}
