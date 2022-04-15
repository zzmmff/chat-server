package org.itstack.naive.chat.util;

import io.netty.channel.Channel;
import org.itstack.naive.chat.protocol.file.dto.FileBurstInstruct;
import org.itstack.naive.chat.protocol.msg.MsgRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName CacheUtil
 * @Description 缓存断点 文件名
 * @Author 张慕帆
 * @Date 1:48 PM 4/9/2022
 * @Version 1.0
 **/
public class CacheUtil {
    public static Map<String, FileBurstInstruct> burstDataMap = new ConcurrentHashMap<>();
    public static Map<Channel, MsgRequest> fileUserMap = new ConcurrentHashMap<>();
    //存储文件名称
    public static Map<Channel,String> channelStringMap = new ConcurrentHashMap<>();
}
