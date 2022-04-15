package org.itstack.naive.chat.domain.user.model;

import java.util.Date;

/**
 * @ClassName FileRecordInfo
 * @Description TODO
 * @Author 张慕帆
 * @Date 10:33 PM 4/9/2022
 * @Version 1.0
 **/
public class FileRecordInfo {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    public String getClientFilePath() {
        return clientFilePath;
    }

    public void setClientFilePath(String clientFilePath) {
        this.clientFilePath = clientFilePath;
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public FileRecordInfo() {
    }

    public FileRecordInfo(String userId, String friendId, String fileMD5, String clientFilePath, Date creatDate, Date updateDate, Integer isDelete) {
        this.userId = userId;
        this.friendId = friendId;
        this.fileMD5 = fileMD5;
        this.clientFilePath = clientFilePath;
        this.creatDate = creatDate;
        this.updateDate = updateDate;
        this.isDelete = isDelete;
    }
    private String userId; //用户id
    private String friendId; //好友ID
    private String fileMD5;  //文件的MD5值
    private String clientFilePath; //文件在客户端的路径
    private String fileSize; //文件大小
    private Date creatDate;
    private Date updateDate;
    private Integer isDelete;   //0 :未被删除 1：被删除
    private String filePath;    //在服务器的路径

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
