package org.itstack.naive.chat.socket.handler;

import io.netty.channel.Channel;
import org.itstack.naive.chat.application.UserService;
import org.itstack.naive.chat.domain.user.model.FileRecordInfo;
import org.itstack.naive.chat.protocol.file.DownLoadFileRequest;
import org.itstack.naive.chat.protocol.file.dto.Constants;
import org.itstack.naive.chat.protocol.file.dto.FileBurstData;
import org.itstack.naive.chat.protocol.file.dto.FileBurstInstruct;
import org.itstack.naive.chat.socket.MyBizHandler;
import org.itstack.naive.chat.util.CacheUtil;
import org.itstack.naive.chat.util.FileUtil;
import org.itstack.naive.chat.util.MsgUtil;

import java.io.File;

/**
 * @ClassName DownLoadFileHandler
 * @Description 处理下载文件请求
 * @Author 张慕帆
 * @Date 9:20 AM 4/11/2022
 * @Version 1.0
 **/
public class DownLoadFileHandler extends MyBizHandler<DownLoadFileRequest> {
    public DownLoadFileHandler(UserService userService) {
        super(userService);
    }

    @Override
    public void channelRead(Channel channel, DownLoadFileRequest msg) throws Exception {
        switch (msg.getTransferType()) {
            case 3:   //收到客户端下载请求
                FileRecordInfo fileRecordInfo = userService.queryFileRecord((String) msg.getTransferObj());
                logger.info("查询文件md5{}",fileRecordInfo.getFileMD5());
                DownLoadFileRequest downLoadFileRequest = MsgUtil.buildResponseDownloadFile(fileRecordInfo.getFilePath(), fileRecordInfo.getClientFilePath(), new File(fileRecordInfo.getClientFilePath()).getName(), Long.parseLong(fileRecordInfo.getFileSize()),fileRecordInfo.getFileMD5());
                //发送待传输文件描述信息 code : 0
                CacheUtil.channelStringMap.put(channel,fileRecordInfo.getClientFilePath());
                channel.writeAndFlush(downLoadFileRequest);
                break;
            case 1: //收到客户端文件下载指令
                FileBurstInstruct fileBurstInstruct = (FileBurstInstruct) msg.getTransferObj();
                if (Constants.FileStatus.COMPLETE == fileBurstInstruct.getStatus()){ //发送完成
                    channel.flush();
                    return;
                }
                //继续发送 code 2
                FileBurstData fileBurstData = FileUtil.readFile(fileBurstInstruct.getFileUrl(), fileBurstInstruct.getReadPosition());
                if (CacheUtil.channelStringMap.get(channel) == null){
                    logger.info("channel 为空");
                }
                fileBurstData.setFileClientUrl(CacheUtil.channelStringMap.get(channel));
                channel.writeAndFlush(MsgUtil.buildDownLoadData(fileBurstData));
                break;
            default:
                break;
        }
    }
}
