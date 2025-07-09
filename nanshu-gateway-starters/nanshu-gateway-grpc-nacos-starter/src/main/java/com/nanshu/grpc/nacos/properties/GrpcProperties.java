package com.nanshu.grpc.nacos.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 南顾北衫
 * @date 2025/7/8
 */
@ConfigurationProperties(prefix = "grpc")
@Data
public class GrpcProperties {
    private int port;
}
