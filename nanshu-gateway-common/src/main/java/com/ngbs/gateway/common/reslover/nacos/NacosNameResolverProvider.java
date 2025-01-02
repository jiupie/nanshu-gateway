package com.ngbs.gateway.common.reslover.nacos;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;

import static com.alibaba.nacos.api.PropertyKeyConst.SERVER_ADDR;

public class NacosNameResolverProvider extends NameResolverProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String NACOS = "nacos";

    private final URI uri;

    public NacosNameResolverProvider(URI targetUri) {
        this.uri = targetUri;
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
        return new NacosNameResolver(properties, targetUri, buildNamingService(properties));
    }

    @Override
    public String getDefaultScheme() {
        return NACOS;
    }

    private NamingService buildNamingService(Properties properties) {
        NamingService namingService = null;

        String serverAddr = uri.getHost() + ":" + uri.getPort();
        properties.put(SERVER_ADDR, serverAddr);

        try {
            namingService = NacosFactory.createNamingService(properties);
        } catch (NacosException e) {
            logger.error("build naming service error, msg: {}", e.getErrMsg());
        }
        return namingService;
    }

}