package com.ngbs.nanshu.gateway.comet.server;


import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 南顾北衫
 */
public class RemotingUtil {
    public static final Logger log = LoggerFactory.getLogger(RemotingUtil.class);
    public static final String OS_NAME = System.getProperty("os.name");
    private static final Map<String, Channel> CHANNEL_MAP = new HashMap<>(1024);
    public static final AttributeKey<String> ID_ATTR = AttributeKey.newInstance("channel_id");
    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;

    static {
        if (OS_NAME != null && OS_NAME.toLowerCase().contains("linux")) {
            isLinuxPlatform = true;
        }

        if (OS_NAME != null && OS_NAME.toLowerCase().contains("winodws")) {
            isWindowsPlatform = true;
        }
    }

    public static boolean isLinuxPlatform() {
        return isLinuxPlatform;
    }

    public static String addChannel(Channel channel) {
        String id = UUID.randomUUID().toString();
        Attribute<String> attr = channel.attr(ID_ATTR);
        attr.set(id);
        CHANNEL_MAP.put(id, channel);
        return id;
    }

    public static Channel getChannel(String channelId) {
        return CHANNEL_MAP.get(channelId);
    }

    public static String closeChannel(Channel channel) {
        String channelId = null;

        if (channel.hasAttr(ID_ATTR)) {
            Attribute<String> attr = channel.attr(ID_ATTR);
            channelId = attr.get();
            CHANNEL_MAP.remove(channelId);
        }
        channel.close();
        return channelId;
    }

    public static String getChannelId(Channel channel) {
        String channelId = null;

        if (channel.hasAttr(ID_ATTR)) {
            Attribute<String> attr = channel.attr(ID_ATTR);
            channelId = attr.get();
        }
        return channelId;
    }

    public static Map<String, String> parseQuery(String uri) {
        int index = uri.indexOf('?');
        HashMap<String, String> parameterMap = new HashMap<>();
        if (index == uri.length() - 1) {
            return parameterMap;
        }
        String query = uri.substring(index + 1);
        String[] kv = query.split("&");
        for (String value : kv) {
            String[] split = value.split("=");
            if (split.length == 2) {
                parameterMap.put(split[0], split[1]);
            }
        }
        return parameterMap;
    }
}
