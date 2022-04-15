package org.itstack.naive.chat.util;

import org.itstack.naive.chat.protocol.file.DownLoadFileRequest;
import org.itstack.naive.chat.protocol.file.FileTransferProtocol;
import org.itstack.naive.chat.protocol.file.dto.*;

/**
 * @ClassName MsgUtil
 * @Description 文件消息构建工具
 * @Author 张慕帆
 * @Date 1:25 PM 4/9/2022
 * @Version 1.0
 **/
public class MsgUtil {
    /**
     * 构建对象；请求传输文件(客户端)
     *
     * @param fileUrl  客户端文件地址
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @return 传输协议
     */
    public static FileTransferProtocol buildRequestTransferFile(String fileUrl, String fileName, Long fileSize) {

        FileDescInfo fileDescInfo = new FileDescInfo();
        fileDescInfo.setFileUrl(fileUrl);
        fileDescInfo.setFileName(fileName);
        fileDescInfo.setFileSize(fileSize);

        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(0);//0请求传输文件、1文件传输指令、2文件传输数据
        fileTransferProtocol.setTransferObj(fileDescInfo);

        return fileTransferProtocol;

    }

    /**
     * 构建对象；文件传输指令(服务端)
     *
     * @param status        0请求传输文件、1文件传输指令、2文件传输数据
     * @param clientFileUrl 客户端文件地址
     * @param readPosition  读取位置
     * @return 传输协议
     */
    public static FileTransferProtocol buildTransferInstruct(Integer status, String clientFileUrl, Long readPosition) {

        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(status);
        fileBurstInstruct.setClientFileUrl(clientFileUrl);
        fileBurstInstruct.setReadPosition(readPosition);

        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT); //0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
        fileTransferProtocol.setTransferObj(fileBurstInstruct);

        return fileTransferProtocol;
    }

    /**
     * 构建对象；文件传输指令(服务端)
     *
     * @return 传输协议
     */
    public static FileTransferProtocol buildTransferInstruct(FileBurstInstruct fileBurstInstruct) {
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);  //0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        return fileTransferProtocol;
    }

    /**
     * 构建对象；文件传输数据(客户端)
     *
     * @return 传输协议
     */
    public static FileTransferProtocol buildTransferData(FileBurstData fileBurstData) {
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(Constants.TransferType.DATA); //0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
        fileTransferProtocol.setTransferObj(fileBurstData);
        return fileTransferProtocol;
    }

    /**
     * 构建对象：请求下载文件(客户端)
     *
     * @retuen 传输协议
     */

    public static DownLoadFileRequest buildRequestDownloadFile(String md5) {
        DownLoadFileRequest downLoadFileRequest = new DownLoadFileRequest();
        downLoadFileRequest.setTransferObj(md5);
        downLoadFileRequest.setTransferType(Constants.TransferType.MD5);
        return downLoadFileRequest;
    }

    /**
     * 构建对象 ：回应下载文件(服务端)
     *
     * @param fileUrl   文件在服务器中的位置
     * @param clientUrl 文件在客户端中的位置
     * @param fileName  文件在客户端中的名字
     * @param fileSize  文件大小
     */
    public static DownLoadFileRequest buildResponseDownloadFile(String fileUrl, String clientUrl, String fileName, Long fileSize,String md5) {
        DownLoadFileDescInfo downLoadFileDescInfo = new DownLoadFileDescInfo();
        downLoadFileDescInfo.setFileUrl(fileUrl);
        downLoadFileDescInfo.setClientUrl(clientUrl);
        downLoadFileDescInfo.setFileName(fileName);
        downLoadFileDescInfo.setFileSize(fileSize);
        downLoadFileDescInfo.setMd5(md5);

        DownLoadFileRequest downLoadFileRequest = new DownLoadFileRequest();
        downLoadFileRequest.setTransferType(0);
        downLoadFileRequest.setTransferObj(downLoadFileDescInfo);
        return downLoadFileRequest;
    }

    /**
     * 构建对象；文件传输指令(客户端)
     *
     * @param status        0请求传输文件、1文件传输指令、2文件传输数据
     * @param filePath 客户端文件地址
     * @param readPosition  读取位置
     * @return 传输协议
     */
    public static DownLoadFileRequest buildDownloadInstruct(Integer status, String filePath, Long readPosition) {
        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(status);
        fileBurstInstruct.setFileUrl(filePath);
        fileBurstInstruct.setReadPosition(readPosition);

        DownLoadFileRequest downLoadFileRequest = new DownLoadFileRequest();
        downLoadFileRequest.setTransferType(Constants.TransferType.INSTRUCT);
        downLoadFileRequest.setTransferObj(fileBurstInstruct);
        return downLoadFileRequest;
    }

    public static DownLoadFileRequest buildDownloadInstruct(FileBurstInstruct fileBurstInstruct){
        DownLoadFileRequest downLoadFileRequest = new DownLoadFileRequest();
        downLoadFileRequest.setTransferType(Constants.TransferType.INSTRUCT);
        downLoadFileRequest.setTransferObj(fileBurstInstruct);
        return downLoadFileRequest;
    }

    /**
     *  构建对象 ： 文件传输数据(客户端)
     * */
    public static DownLoadFileRequest buildDownLoadData(FileBurstData fileBurstData){
        DownLoadFileRequest downLoadFileRequest = new DownLoadFileRequest();
        downLoadFileRequest.setTransferType(Constants.TransferType.DATA);
        downLoadFileRequest.setTransferObj(fileBurstData);
        return downLoadFileRequest;
    }

}
