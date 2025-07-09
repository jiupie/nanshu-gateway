package com.nanshu.grpc.nacos;

import com.alibaba.nacos.api.naming.NamingService;
import com.nanshu.grpc.nacos.properties.GrpcProperties;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import java.net.URI;

public class NacosNameResolverProvider extends NameResolverProvider {

    protected static final String NACOS = "nacos";
    private final GrpcProperties grpcProperties;
    private final NamingService namingService;

    public NacosNameResolverProvider(NamingService namingService, GrpcProperties grpcProperties) {
        this.namingService = namingService;
        this.grpcProperties = grpcProperties;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }


    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        return new NacosNameResolver(targetUri, namingService, grpcProperties);
    }

    @Override
    public String getDefaultScheme() {
        return NACOS;
    }

}