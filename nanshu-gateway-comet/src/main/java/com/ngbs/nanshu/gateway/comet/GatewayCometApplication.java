package com.ngbs.nanshu.gateway.comet;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author 南顾北衫
 * @date 2024/8/8
 */
@SpringBootApplication
public class GatewayCometApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .web(WebApplicationType.NONE)
                .sources(GatewayCometApplication.class)
                .run(args);
    }

}
