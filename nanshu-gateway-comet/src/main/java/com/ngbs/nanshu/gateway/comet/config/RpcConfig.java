package com.ngbs.nanshu.gateway.comet.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.ngbs.gateway.common.properties.NacosDiscoveryProperties;
import com.ngbs.gateway.common.reslover.nacos.NacosNameResolverProvider;
import com.ngbs.nashu.gateway.logic.api.LogicConnectGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Properties;

import static com.alibaba.nacos.api.PropertyKeyConst.*;


/**
 * @author 南顾北衫
 * @date 2024/8/24
 */
@EnableConfigurationProperties(NacosDiscoveryProperties.class)
@Slf4j
@Component
public class RpcConfig {

    @Value("${grpc.logic-service}")
    private String logicServiceName;

    @Bean
    public ManagedChannel getGatewayConnect(NamingService namingService) {
        NameResolverRegistry.getDefaultRegistry().register(new NacosNameResolverProvider(namingService));
        return ManagedChannelBuilder.forTarget(logicServiceName).usePlaintext().build();
    }

    @Bean
    public LogicConnectGrpc.LogicConnectBlockingStub logicConnectGrpc(ManagedChannel managedChannel) {
        return LogicConnectGrpc.newBlockingStub(managedChannel);
    }

    @Bean
    public NamingService namingService(NacosDiscoveryProperties nacosDiscoveryProperties) {
        NamingService namingService = null;
        Properties properties = new Properties();
        URI uri = URI.create(nacosDiscoveryProperties.getServerAddr());
        String serverAddr = uri.getHost() + ":" + uri.getPort();
        properties.put(SERVER_ADDR, serverAddr);
        properties.put(NAMESPACE, nacosDiscoveryProperties.getNamespace());
        properties.put(SERVER_NAME, nacosDiscoveryProperties.getServiceName());

        try {
            namingService = NacosFactory.createNamingService(properties);
        } catch (NacosException e) {
            log.error("build naming service error, msg: {}", e.getErrMsg());
        }
        return namingService;
    }
}
