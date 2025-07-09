package com.ngbs.nanshu.gateway.comet.rpc;

import com.ngbs.nanshu.gateway.comet.api.CometMsgGrpc;
import com.ngbs.nanshu.gateway.comet.api.CometService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @author 南顾北衫
 * @date 2024/8/24
 */
@GrpcService
public class CometMsgImpl extends CometMsgGrpc.CometMsgImplBase {
    @Override
    public void pushMsg(CometService.PushMsgReq request, StreamObserver<CometService.PushMsgResponse> responseObserver) {

    }

    @Override
    public void broadcastRoom(CometService.BroadcastRoomReq request, StreamObserver<CometService.BroadcastRoomReply> responseObserver) {

    }
}
