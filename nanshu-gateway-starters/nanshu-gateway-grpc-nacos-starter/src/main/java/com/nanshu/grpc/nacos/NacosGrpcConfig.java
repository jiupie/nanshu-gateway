package com.nanshu.grpc.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.nanshu.grpc.nacos.properties.GrpcProperties;
import com.nanshu.grpc.nacos.properties.NacosDiscoveryProperties;
import io.grpc.NameResolverRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.Properties;

import static com.alibaba.nacos.api.PropertyKeyConst.*;

/**
 * @author 南顾北衫
 * @date 2025/7/8
 */
@Slf4j
@EnableConfigurationProperties(value = {NacosDiscoveryProperties.class, GrpcProperties.class})
@Configuration
public class NacosGrpcConfig {

    @Bean
    public NamingService namingService(NacosDiscoveryProperties nacosDiscoveryProperties, GrpcProperties grpcProperties) {
        NamingService namingService = null;
        Properties properties = new Properties();
        URI uri = URI.create(nacosDiscoveryProperties.getServerAddr());
        String serverAddr = uri.getHost() + ":" + uri.getPort();
        properties.put(SERVER_ADDR, serverAddr);
        properties.put(NAMESPACE, nacosDiscoveryProperties.getNamespace());
        properties.put(SERVER_NAME, nacosDiscoveryProperties.getServiceName());
        try {
            namingService = NacosFactory.createNamingService(properties);
            NameResolverRegistry.getDefaultRegistry().register(new NacosNameResolverProvider(namingService, grpcProperties));
        } catch (NacosException e) {
            log.error("build naming service error, msg: {}", e.getErrMsg());
        }
        return namingService;
    }

    @Bean
    public NacosRegisterRunner nacosRegisterRunner() {
        return new NacosRegisterRunner();
    }
}
