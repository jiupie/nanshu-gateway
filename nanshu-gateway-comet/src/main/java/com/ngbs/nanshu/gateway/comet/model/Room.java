package com.ngbs.nanshu.gateway.comet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Room {
    private List<String> channelIdList;

    private ReadWriteLock readWriteLock;

    private String roomId;

    public Room(String roomId) {
        this.roomId = roomId;
        this.channelIdList = new ArrayList<>();
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    public void put(String channelId) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            channelIdList.add(channelId);
        } finally {
            lock.unlock();
        }
    }

    public void del(String channelId) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            channelIdList.remove(channelId);
        } finally {
            lock.unlock();
        }
    }

    public List<String> getChannelIdList() {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try {
            return channelIdList;
        } finally {
            lock.unlock();
        }
    }

    public int channelSize() {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try {
            return channelIdList.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(channelIdList, room.channelIdList) && Objects.equals(readWriteLock, room.readWriteLock) && Objects.equals(roomId, room.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelIdList, readWriteLock, roomId);
    }
}
