package com.ngbs.nanshu.gateway.logic.constant;

/**
 * @author 南顾北衫
 * @date 2024/8/11
 */
public class RedisKey {

    public final static String WORK_ID = "ns:gateway:workId";
    private final static String KEY_ID = "ns:conn:";

    public static String connectKey(String key) {
        return KEY_ID + key;
    }


}
