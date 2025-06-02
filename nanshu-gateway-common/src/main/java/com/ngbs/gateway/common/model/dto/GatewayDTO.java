package com.ngbs.gateway.common.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
@Data
public class GatewayDTO<T> {
    List<String> channelIds;
    private T data;
}
