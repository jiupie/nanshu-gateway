package com.ngbs.nanshu.gateway.comet.rpc;

import com.ngbs.nanshu.gateway.comet.api.CometMsg;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author 南顾北衫
 * @date 2024/8/24
 */
@DubboService
public class CometMsgImpl implements CometMsg {
    @Override
    public void pushMsg() {

    }
}
