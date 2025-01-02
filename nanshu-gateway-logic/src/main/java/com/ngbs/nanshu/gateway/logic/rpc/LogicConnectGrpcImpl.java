package com.ngbs.nanshu.gateway.logic.rpc;

import com.ngbs.nanshu.gateway.logic.constant.RedisKey;
import com.ngbs.nanshu.gateway.logic.utils.IdGenerator;
import com.ngbs.nanshu.gateway.logic.utils.RedisService;
import com.ngbs.nashu.gateway.logic.api.LogicConnectGrpc;
import com.ngbs.nashu.gateway.logic.api.LogicConnectService;
import io.grpc.stub.StreamObserver;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 南顾北衫
 * @date 2024/8/24
 */
public class LogicConnectGrpcImpl extends LogicConnectGrpc.LogicConnectImplBase {
    @Resource
    private IdGenerator idGenerator;

    @Resource
    private RedisService redisService;

    public LogicConnectGrpcImpl(IdGenerator idGenerator, RedisService redisService) {
        this.idGenerator = idGenerator;
        this.redisService = redisService;
    }

    @Override
    public void connect(LogicConnectService.ConnectReq request, StreamObserver<LogicConnectService.ConnectResponse> responseObserver) {
        if ("123456".equals(request.getToken())) {
            String key = idGenerator.generateId();
            redisService.set(RedisKey.connectKey(key), request.getServerId(), 70, TimeUnit.SECONDS);
            LogicConnectService.ConnectResponse connectResponse = LogicConnectService.ConnectResponse.newBuilder()
                    .setUserId("12345")
                    .setKey(key)
                    .build();
            responseObserver.onNext(connectResponse);
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void heartBeat(LogicConnectService.HeartbeatRequest request, StreamObserver<LogicConnectService.HeartbeatResponse> responseObserver) {
        String key = request.getKey();
        redisService.expire(RedisKey.connectKey(key), 70, TimeUnit.SECONDS);
        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    @Override
    public void disConnect(LogicConnectService.DisconnectReq request, StreamObserver<LogicConnectService.DisconnectResponse> responseObserver) {
        String key = request.getKey();
        redisService.del(RedisKey.connectKey(key));
        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }


}
