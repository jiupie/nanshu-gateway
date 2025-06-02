package com.ngbs.nanshu.gateway.comet.config;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.utils.NetUtils;
import com.ngbs.gateway.common.properties.NacosDiscoveryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author 南顾北衫
 * @date 2025/6/2
 */
@Component
public class NacosRegisterConfig implements ApplicationRunner {

    @Autowired
    private NamingService namingService;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Value("${grpc.port}")
    private Integer grpcPort;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Instance instance = new Instance();
        instance.setIp(NetUtils.localIP());
        instance.setPort(grpcPort);
        namingService.registerInstance(nacosDiscoveryProperties.getServiceName(), instance);
    }
}
