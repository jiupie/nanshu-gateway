package com.ngbs.nanshu.gateway.comet.service;

public interface RoomService {
    void put(String roomId, String channelId);

    void del(String roomId, String channelId);
}
