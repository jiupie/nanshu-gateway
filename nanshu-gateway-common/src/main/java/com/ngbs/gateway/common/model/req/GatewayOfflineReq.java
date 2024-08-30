package com.ngbs.gateway.common.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {"type":"connect","userId":"3","username":"wuddddwenhao","routeType":"DOCX","routeKey":"HUgAdIBqSoPKcyxQgIicvRYWnyg"}
 *
 * @author 南顾北衫
 * @date 2024/8/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GatewayOfflineReq extends GatewayBaseReq {
    public GatewayOfflineReq() {
        this.type = GatewayTypeEnum.OFFLINE.getName();
    }
}
