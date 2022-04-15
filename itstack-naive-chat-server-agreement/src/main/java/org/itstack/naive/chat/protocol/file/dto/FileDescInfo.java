package org.itstack.naive.chat.protocol.file.dto;

/**
 * @ClassName FileDescInfo
 * @Description 文件描述信息
 * @Author 张慕帆
 * @Date 1:19 PM 4/9/2022
 * @Version 1.0
 **/
public class FileDescInfo {
    private String fileUrl;
    private String fileName;
    private Long fileSize;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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
}
