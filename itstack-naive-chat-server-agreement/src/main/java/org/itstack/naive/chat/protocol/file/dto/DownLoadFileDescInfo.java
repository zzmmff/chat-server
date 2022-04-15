package org.itstack.naive.chat.protocol.file.dto;

/**
 * @ClassName DownLoadFileDescInfo
 * @Description TODO
 * @Author 张慕帆
 * @Date 6:09 PM 4/11/2022
 * @Version 1.0
 **/
public class DownLoadFileDescInfo {
    private String fileUrl;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    private String clientUrl;
    private String fileName;
    private Long fileSize;
    private String md5;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
