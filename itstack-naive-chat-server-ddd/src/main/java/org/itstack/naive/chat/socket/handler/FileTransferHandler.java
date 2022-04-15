package org.itstack.naive.chat.socket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import org.apache.ibatis.cache.Cache;
import org.itstack.naive.chat.application.UserService;
import org.itstack.naive.chat.domain.user.model.ChatRecordInfo;
import org.itstack.naive.chat.domain.user.model.FileRecordInfo;
import org.itstack.naive.chat.infrastructure.common.SocketChannelUtil;
import org.itstack.naive.chat.protocol.file.FileTransferProtocol;
import org.itstack.naive.chat.protocol.file.dto.Constants;
import org.itstack.naive.chat.protocol.file.dto.FileBurstData;
import org.itstack.naive.chat.protocol.file.dto.FileBurstInstruct;
import org.itstack.naive.chat.protocol.file.dto.FileDescInfo;
import org.itstack.naive.chat.protocol.msg.MsgRequest;
import org.itstack.naive.chat.protocol.msg.MsgResponse;
import org.itstack.naive.chat.socket.MyBizHandler;
import org.itstack.naive.chat.util.CacheUtil;
import org.itstack.naive.chat.util.FileUtil;
import org.itstack.naive.chat.util.MsgUtil;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName FileTransferHandler
 * @Description 处理文件传输事件 接受中转文件
 * @Author 张慕帆
 * @Date 4:55 PM 4/9/2022
 * @Version 1.0
 **/
public class FileTransferHandler  extends MyBizHandler<FileTransferProtocol> {

    public FileTransferHandler (UserService userService){super(userService);}

    @Override
    public void channelRead(Channel channel, FileTransferProtocol msg) throws Exception{
        switch (msg.getTransferType()){
            case 0: //文件描述信息
                FileDescInfo fileDescInfo = (FileDescInfo) msg.getTransferObj();
                //TODO 处理断点信息
                FileTransferProtocol sendFileTransferProtocol = MsgUtil.buildTransferInstruct(Constants.FileStatus.BEGIN, fileDescInfo.getFileUrl(), 0L);

                channel.writeAndFlush(sendFileTransferProtocol);
                logger.info("文件发送请求{}", JSON.toJSONString(msg));
                break;
            case 2:   //文件分片指令
                FileBurstData fileBurstData = (FileBurstData) msg.getTransferObj();
                String path = String.format("%s\\Servers\\Chat Server\\Files", System.getProperty("user.home"));
                File file = new File(path);
                if (!file.exists()){
                    file.mkdirs();
                }
                logger.info("服务端目录: {}",path);
                FileBurstInstruct fileBurstInstruct = FileUtil.writeFile(path, fileBurstData);
                //TODO 保存断点信息
                channel.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstruct));
                logger.info("{} 接受客户端数据",new Date());
                if (Constants.FileStatus.COMPLETE == fileBurstInstruct.getStatus()){ //发送完成
                    //计算文件md5 并且落库  32位Hex
                    File fileTemp = new File(path + "/" + fileBurstData.getFileName());
                    logger.info("文件MD5 : {}",fileBurstInstruct.getMd5());
                    FileRecordInfo fileRecordInfo = new FileRecordInfo();
                    MsgRequest msgRequest = CacheUtil.fileUserMap.get(channel);
                    CacheUtil.fileUserMap.remove(channel);
                    logger.info("消息信息 {}",JSON.toJSONString(msgRequest));
                    JSONObject jsonObject = JSON.parseObject(msgRequest.getMsgText());
                    fileRecordInfo.setUserId(msgRequest.getUserId());
                    fileRecordInfo.setFriendId(msgRequest.getFriendId());
                    fileRecordInfo.setFileMD5(fileBurstInstruct.getMd5());
                    fileRecordInfo.setClientFilePath((String) jsonObject.get("clientFilePath"));
                    fileRecordInfo.setFilePath(path + "\\"+fileBurstInstruct.getMd5());
                    fileRecordInfo.setIsDelete(0);
                    fileRecordInfo.setFileSize(String.valueOf(fileTemp.length()));
                    //文件上传成功，发送消息
                    Map <String,String>map = new ConcurrentHashMap();
                    map.put("filePath",fileTemp.getAbsolutePath());
                    map.put("md5",fileBurstInstruct.getMd5());
                    map.put("fileName",fileTemp.getName());
                    map.put("fileSize", String.valueOf(fileTemp.length()));
                    map.put("clientFilePath",fileRecordInfo.getClientFilePath());
                    String msgText = JSON.toJSONString(map);

                    //修改服务器中的文件名 以md5命名
                    fileTemp.renameTo(new File(path + "/" +fileBurstInstruct.getMd5()));
                    //异步写入文件记录
                    userService.asyncAppendFileRecord(fileRecordInfo);
                    //异步写入消息记录
                    userService.asyncAppendChatRecord(new ChatRecordInfo(msgRequest.getUserId(),msgRequest.getFriendId(),msgText,msgRequest.getMsgType(),msgRequest.getMsgDate()));
                    //添加对话框[如果对方没有你的对话框就添加]
                    userService.addTalkBoxInfo(msgRequest.getFriendId(),msgRequest.getUserId(), org.itstack.naive.chat.infrastructure.common.Constants.TalkType.Friend.getCode());
                    Channel friendChannel = SocketChannelUtil.getChannel(msgRequest.getFriendId());
                    if (null == friendChannel){
                       logger.info("用户id： {} 未登录",msgRequest.getFriendId());
                       return;
                    }
                    //发送消息
                    friendChannel.writeAndFlush(new MsgResponse(msgRequest.getUserId(),msgText,msgRequest.getMsgType(),msgRequest.getMsgDate()));
                }
                break;
            default:
                break;
        }
    }
}
