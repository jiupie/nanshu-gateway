package com.ngbs.gateway.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 南顾北衫
 * @date 2025/6/2
 */
@Data
@ConfigurationProperties(prefix = "nacos")
public class NacosDiscoveryProperties {
    private String serverAddr = "127.0.0.1:8848";

    private String namespace = "public";

    private String serviceName;


}
