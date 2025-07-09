package com.nanshu.grpc.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.utils.NetUtils;
import com.nanshu.grpc.nacos.properties.GrpcProperties;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class NacosNameResolver extends NameResolver {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String serviceName;
    private final NamingService namingService;
    private final GrpcProperties grpcProperties;

    public NacosNameResolver(URI targetUri, NamingService namingService, GrpcProperties grpcProperties) {
        this.serviceName = targetUri.getAuthority();
        this.namingService = namingService;
        this.grpcProperties = grpcProperties;
    }

    @Override
    public String getServiceAuthority() {
        return serviceName;
    }

    @Override
    public void start(Listener listener) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            update(listener, instances);
            namingService.subscribe(serviceName, event -> {
                NamingEvent namingEvent = (NamingEvent) event;
                logger.info("subscribe event: {}", event);
                update(listener, namingEvent.getInstances());
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
        try {
            namingService.deregisterInstance(serviceName, NetUtils.localIP(),grpcProperties.getPort());
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Listener listener, List<Instance> instances) {
        List<EquivalentAddressGroup> equivalentAddressGroups = instances.stream().map(instance -> {
            int port = instance.getPort();
            return new EquivalentAddressGroup(new InetSocketAddress(instance.getIp(), port));
        }).collect(Collectors.toList());
        listener.onAddresses(equivalentAddressGroups, Attributes.EMPTY);
    }

}