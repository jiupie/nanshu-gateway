package com.ngbs.gateway.common.model.req;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author 南顾北衫
 * @date 2024/8/10
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = GatewayBaseReq.class
)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = GatewayConnectReq.class, name = "connect"),
        @JsonSubTypes.Type(value = GatewayOfflineReq.class, name = "offline")}
)
@Data
public class GatewayBaseReq {
    private String channelId;
    protected String type;
}
