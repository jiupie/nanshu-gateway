package com.nanshu.grpc.nacos;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 南顾北衫
 * @date 2025/7/9
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import(NacosGrpcConfig.class)
public @interface EnableGrpcNacos {
}
