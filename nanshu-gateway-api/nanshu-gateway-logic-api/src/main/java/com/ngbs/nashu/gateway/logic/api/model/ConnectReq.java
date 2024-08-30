package com.ngbs.nashu.gateway.logic.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 南顾北衫
 * @date 2024/8/26
 */
@Data
public class ConnectReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private String server;

}
