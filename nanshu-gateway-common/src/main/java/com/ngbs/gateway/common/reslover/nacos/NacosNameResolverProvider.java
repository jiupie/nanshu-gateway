package com.ngbs.gateway.common.reslover.nacos;


import com.alibaba.nacos.api.naming.NamingService;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import java.net.URI;
import java.util.Properties;

public class NacosNameResolverProvider extends NameResolverProvider {

    protected static final String NACOS = "nacos";
    private NamingService namingService;

    public NacosNameResolverProvider(NamingService namingService) {
        this.namingService = namingService;
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
        Properties properties = new Properties();
        return new NacosNameResolver(properties, targetUri, namingService);
    }

    @Override
    public String getDefaultScheme() {
        return NACOS;
    }

}