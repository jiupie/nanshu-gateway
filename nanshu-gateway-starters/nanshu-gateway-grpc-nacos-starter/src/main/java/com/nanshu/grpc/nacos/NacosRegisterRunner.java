package com.nanshu.grpc.nacos;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.utils.NetUtils;
import com.nanshu.grpc.nacos.properties.GrpcProperties;
import com.nanshu.grpc.nacos.properties.NacosDiscoveryProperties;
import io.grpc.NameResolverRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * @author 南顾北衫
 * @date 2025/6/2
 */
@Slf4j
public class NacosRegisterRunner implements ApplicationRunner {

    @Autowired
    private NamingService namingService;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired
    private GrpcProperties grpcProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Instance instance = new Instance();
        instance.setIp(NetUtils.localIP());
        instance.setPort(grpcProperties.getPort());
        namingService.registerInstance(nacosDiscoveryProperties.getServiceName(), instance);
    }

}
