package com.ngbs.gateway.common.model.req;

import lombok.Getter;

/**
 * @author 南顾北衫
 * @date 2024/8/10
 */
@Getter
public enum GatewayTypeEnum {

    CONNECT("connect"),
    OFFLINE("offline");
    private final String name;

    GatewayTypeEnum(String name) {


        this.name = name;
    }
}
