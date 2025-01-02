package com.ngbs.nanshu.gateway.comet.service.impl;

import com.ngbs.nanshu.gateway.comet.model.Room;
import com.ngbs.nanshu.gateway.comet.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomServiceImpl implements RoomService {
    private static final ConcurrentHashMap<String, Room> ROOM_MAP = new ConcurrentHashMap<>();


    @Override
    public void put(String roomId, String channelId) {
        Room room = ROOM_MAP.computeIfAbsent(roomId, Room::new);
        room.put(channelId);
    }


    @Override
    public void del(String roomId, String channelId) {
        Room room = ROOM_MAP.get(roomId);
        if (room != null) {
            room.del(channelId);
            if (room.channelSize() == 0) {
                ROOM_MAP.remove(roomId);
            }
        }
    }
}
