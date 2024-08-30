package com.ngbs.gateway.common.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {"type":"connect","userId":1,"username":"admin"}
 *
 * @author 南顾北衫
 * @date 2024/8/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GatewayConnectReq extends GatewayBaseReq {
    private Long userId;
    private String username;
    private String routeType;
    private String routeKey;

}
