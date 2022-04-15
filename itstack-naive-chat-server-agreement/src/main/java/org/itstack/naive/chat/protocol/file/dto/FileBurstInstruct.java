package org.itstack.naive.chat.protocol.file.dto;

/**
 * @ClassName FileBurstInstruct
 * @Description 文件分片指令
 * @Author 张慕帆
 * @Date 1:18 PM 4/9/2022
 * @Version 1.0
 **/
public class FileBurstInstruct {
    private Integer status;       //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
    private String clientFileUrl; //客户端文件URL
    private String fileUrl;   //服务端URL  下载文件时使用
    private Long readPosition; //读取位置
    private String md5;  //文件的md5值

    public FileBurstInstruct(){}

    public FileBurstInstruct(Integer status) {
        this.status = status;
    }

    public FileBurstInstruct(Integer status, String md5) {
        this.status = status;
        this.md5 = md5;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClientFileUrl() {
        return clientFileUrl;
    }

    public void setClientFileUrl(String clientFileUrl) {
        this.clientFileUrl = clientFileUrl;
    }

    public Long getReadPosition() {
        return readPosition;
    }

    public void setReadPosition(Long readPosition) {
        this.readPosition = readPosition;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
