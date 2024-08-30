package com.ngbs.nanshu.gateway.logic.constant;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
public class RedisKey {
    //用户和连接的关系
    private static final String USER_CONNECT_KEY = "ws:user:clients:";

    //存储文件和 WebSocket 连接的关系
    private static final String FILE_CONNECT_KEY = "ws:file:clients:";

    //存储当前 WebSocket 连接下的全部用户和文件关系数据，采用 Redis Hash 方式进行存储，对应 key 为 user 和 guid
    public static final String CONNECT_HASH_KEY = "ws:socketId:clients:";

    public static String userConnectKey(String userId) {
        return USER_CONNECT_KEY + userId;
    }

    public static String fileConnectKey(String type, String fileId) {
        return FILE_CONNECT_KEY + type + "_" + fileId;
    }

    public static String fileConnectStr(String type, String fileId) {
        return type + "_" + fileId;
    }

    public static String fileConnectKey(String key) {
        return FILE_CONNECT_KEY + key;
    }

    public static String connectHashKey(String channelId) {
        return CONNECT_HASH_KEY + channelId;
    }

}
