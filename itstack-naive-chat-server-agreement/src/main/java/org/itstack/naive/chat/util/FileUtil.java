package org.itstack.naive.chat.util;

import org.itstack.naive.chat.protocol.file.dto.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName FileUtil
 * @Description 文件读写工具
 * @Author 张慕帆
 * @Date 1:46 PM 4/9/2022
 * @Version 1.0
 **/
public class FileUtil  {

    public  static  MessageDigest fileMD5;

    static {
        try {
            fileMD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public static FileBurstData readFile(String fileUrl, Long readPosition) throws IOException {
        File file = new File(fileUrl);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");//r: 只读模式 rw:读写模式
        randomAccessFile.seek(readPosition);
        byte[] bytes = new byte[(1<<14)];
        int readSize = randomAccessFile.read(bytes);
        if (readSize <= 0) {
            randomAccessFile.close();
            return new FileBurstData(Constants.FileStatus.COMPLETE);//Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
        }
        FileBurstData fileInfo = new FileBurstData();
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileName(file.getName());
        fileInfo.setBeginPos(readPosition);
        fileInfo.setEndPos(readPosition + readSize);
        //不足1024需要拷贝去掉空字节
        if (readSize < (1<<14)) {
            byte[] copy = new byte[readSize];
            System.arraycopy(bytes, 0, copy, 0, readSize);
            fileInfo.setBytes(copy);
            fileInfo.setStatus(Constants.FileStatus.END);
        } else {
            fileInfo.setBytes(bytes);
            fileInfo.setStatus(Constants.FileStatus.CENTER);
        }
        randomAccessFile.close();
        return fileInfo;
    }

    public static FileBurstInstruct writeFile(String baseUrl, FileBurstData fileBurstData) throws IOException, NoSuchAlgorithmException {

        if (null == fileMD5){
            fileMD5 = MessageDigest.getInstance("MD5");
        }

        if (Constants.FileStatus.COMPLETE == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE, ByteUtil.encodeHexString(fileMD5.digest())); //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
        }
        fileMD5.update(fileBurstData.getBytes());
        File file = new File(baseUrl + "/" + fileBurstData.getFileName());
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");//r: 只读模式 rw:读写模式
        randomAccessFile.seek(fileBurstData.getBeginPos());      //移动文件记录指针的位置,
        randomAccessFile.write(fileBurstData.getBytes());        //调用了seek（start）方法，是指把文件的记录指针定位到start字节的位置。也就是说程序将从start字节开始写数据
        randomAccessFile.close();


        if (Constants.FileStatus.END == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE, ByteUtil.encodeHexString(fileMD5.digest())); //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
        }

        //文件分片传输指令
        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(Constants.FileStatus.CENTER);            //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
        fileBurstInstruct.setClientFileUrl(fileBurstData.getFileUrl());      //客户端文件URL
        fileBurstInstruct.setReadPosition(fileBurstData.getEndPos());    //读取位置

        return fileBurstInstruct;
    }

    public static FileBurstInstruct writeFile(String baseUrl, FileBurstData fileBurstData ,String fileClientPath) throws IOException, NoSuchAlgorithmException {

        if (null == fileMD5){
            fileMD5 = MessageDigest.getInstance("MD5");
        }

        if (Constants.FileStatus.COMPLETE == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE, ByteUtil.encodeHexString(fileMD5.digest())); //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
        }
        fileMD5.update(fileBurstData.getBytes());
        String name = new File(fileClientPath).getName();
        File file = new File(baseUrl + "/" + name);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");//r: 只读模式 rw:读写模式
        randomAccessFile.seek(fileBurstData.getBeginPos());      //移动文件记录指针的位置,
        randomAccessFile.write(fileBurstData.getBytes());        //调用了seek（start）方法，是指把文件的记录指针定位到start字节的位置。也就是说程序将从start字节开始写数据
        randomAccessFile.close();


        if (Constants.FileStatus.END == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE, ByteUtil.encodeHexString(fileMD5.digest())); //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
        }

        //文件分片传输指令
        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(Constants.FileStatus.CENTER);            //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
        fileBurstInstruct.setFileUrl(fileBurstData.getFileUrl());      //服务器文件URL
        fileBurstInstruct.setClientFileUrl(fileClientPath);
        fileBurstInstruct.setReadPosition(fileBurstData.getEndPos());    //读取位置

        return fileBurstInstruct;
    }
}
