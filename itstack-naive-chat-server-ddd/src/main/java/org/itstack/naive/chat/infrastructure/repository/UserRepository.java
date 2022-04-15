package org.itstack.naive.chat.infrastructure.repository;

import org.itstack.naive.chat.domain.user.model.*;
import org.itstack.naive.chat.domain.user.repository.IUserRepository;
import org.itstack.naive.chat.infrastructure.dao.*;
import org.itstack.naive.chat.infrastructure.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.itstack.naive.chat.infrastructure.common.Constants.TalkType.Friend;
import static org.itstack.naive.chat.infrastructure.common.Constants.TalkType.Group;

/**
 */
@Repository("userRepository")
public class UserRepository implements IUserRepository {

    @Autowired
    private IChatRecordDao chatRecordDao;
    @Autowired
    private IGroupsDao groupsDao;
    @Autowired
    private ITalkBoxDao talkBoxDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IUserFriendDao userFriendDao;
    @Autowired
    private IUserGroupDao userGroupDao;
    @Autowired
    private IFileRecordDao fileRecordDao;

    @Override
    public String queryUserPassword(String userId) {
        return userDao.queryUserPassword(userId);
    }

    @Override
    public UserInfo queryUserInfo(String userId) {
        User user = userDao.queryUserById(userId);
        return new UserInfo(user.getUserId(), user.getUserNickName(), user.getUserHead());
    }

    @Override
    public List<TalkBoxInfo> queryTalkBoxInfoList(String userId) {
        List<TalkBoxInfo> talkBoxInfoList = new ArrayList<>();
        List<TalkBox> talkBoxList = talkBoxDao.queryTalkBoxList(userId);
        for (TalkBox talkBox : talkBoxList) {
            TalkBoxInfo talkBoxInfo = new TalkBoxInfo();
            if (Friend.getCode().equals(talkBox.getTalkType())) {
                User user = userDao.queryUserById(talkBox.getTalkId());
                talkBoxInfo.setTalkType(Friend.getCode());
                talkBoxInfo.setTalkId(user.getUserId());
                talkBoxInfo.setTalkName(user.getUserNickName());
                talkBoxInfo.setTalkHead(user.getUserHead());
            } else if (Group.getCode().equals(talkBox.getTalkType())) {
                Groups groups = groupsDao.queryGroupsById(talkBox.getTalkId());
                talkBoxInfo.setTalkType(Group.getCode());
                talkBoxInfo.setTalkId(groups.getGroupId());
                talkBoxInfo.setTalkName(groups.getGroupName());
                talkBoxInfo.setTalkHead(groups.getGroupHead());
            }
            talkBoxInfoList.add(talkBoxInfo);
        }
        return talkBoxInfoList;
    }

    @Override
    public void addTalkBoxInfo(String userId, String talkId, Integer talkType) {
        try {
            if (null != talkBoxDao.queryTalkBox(userId, talkId)) return;
            TalkBox talkBox = new TalkBox();
            talkBox.setUserId(userId);
            talkBox.setTalkId(talkId);
            talkBox.setTalkType(talkType);
            talkBoxDao.addTalkBox(talkBox);
        } catch (DuplicateKeyException ignored) {
        }
    }

    @Override
    public List<UserFriendInfo> queryUserFriendInfoList(String userId) {
        List<UserFriendInfo> userFriendInfoList = new ArrayList<>();
        List<String> friendIdList = userFriendDao.queryUserFriendIdList(userId);
        for (String friendId : friendIdList) {
            User user = userDao.queryUserById(friendId);
            UserFriendInfo friendInfo = new UserFriendInfo();
            friendInfo.setFriendId(user.getUserId());
            friendInfo.setFriendName(user.getUserNickName());
            friendInfo.setFriendHead(user.getUserHead());
            userFriendInfoList.add(friendInfo);
        }
        return userFriendInfoList;
    }

    @Override
    public List<GroupsInfo> queryUserGroupInfoList(String userId) {
        List<GroupsInfo> groupsInfoList = new ArrayList<>();
        List<String> groupsIdList = userGroupDao.queryUserGroupsIdList(userId);
        for (String groupsId : groupsIdList) {
            Groups groups = groupsDao.queryGroupsById(groupsId);
            GroupsInfo groupsInfo = new GroupsInfo();
            groupsInfo.setGroupId(groups.getGroupId());
            groupsInfo.setGroupName(groups.getGroupName());
            groupsInfo.setGroupHead(groups.getGroupHead());
            groupsInfoList.add(groupsInfo);
        }
        return groupsInfoList;
    }

    @Override
    public List<LuckUserInfo> queryFuzzyUserInfoList(String userId, String searchKey) {
        List<LuckUserInfo> luckUserInfoList = new ArrayList<>();
        List<User> userList = userDao.queryFuzzyUserList(userId, searchKey);
        for (User user : userList) {
            LuckUserInfo userInfo = new LuckUserInfo(user.getUserId(), user.getUserNickName(), user.getUserHead(), 0);
            UserFriend req = new UserFriend();
            req.setUserId(userId);
            req.setUserFriendId(user.getUserId());
            UserFriend userFriend = userFriendDao.queryUserFriendById(req);
            if (null != userFriend) {
                userInfo.setStatus(2);
            }
            luckUserInfoList.add(userInfo);
        }
        return luckUserInfoList;
    }

    @Override
    public void addUserFriend(List<UserFriend> userFriendList) {
        try {
            userFriendDao.addUserFriendList(userFriendList);
        } catch (DuplicateKeyException ignored) {
        }
    }

    @Override
    public void appendChatRecord(ChatRecordInfo chatRecordInfo) {
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setUserId(chatRecordInfo.getUserId());
        chatRecord.setFriendId(chatRecordInfo.getFriendId());
        chatRecord.setMsgContent(chatRecordInfo.getMsgContent());
        chatRecord.setMsgType(chatRecordInfo.getMsgType());
        chatRecord.setMsgDate(chatRecordInfo.getMsgDate());
        chatRecord.setTalkType(chatRecordInfo.getTalkType());
        chatRecordDao.appendChatRecord(chatRecord);
    }

    @Override
    public List<ChatRecordInfo> queryChatRecordInfoList(String talkId, String userId, Integer talkType) {
        List<ChatRecordInfo> chatRecordInfoList = new ArrayList<>();
        List<ChatRecord> list = new ArrayList<>();
        if (Friend.getCode().equals(talkType)) {
            list = chatRecordDao.queryUserChatRecordList(talkId, userId);
        } else if (Group.getCode().equals(talkType)) {
            list = chatRecordDao.queryGroupsChatRecordList(talkId, userId);
        }
        for (ChatRecord chatRecord : list) {
            ChatRecordInfo chatRecordInfo = new ChatRecordInfo();
            chatRecordInfo.setUserId(chatRecord.getUserId());
            chatRecordInfo.setFriendId(chatRecord.getFriendId());
            chatRecordInfo.setMsgContent(chatRecord.getMsgContent());
            chatRecordInfo.setMsgType(chatRecord.getMsgType());
            chatRecordInfo.setMsgDate(chatRecord.getMsgDate());
            chatRecordInfoList.add(chatRecordInfo);
        }
        return chatRecordInfoList;
    }

    @Override
    public void deleteUserTalk(String userId, String talkId) {
        talkBoxDao.deleteUserTalk(userId, talkId);
    }

    @Override
    public List<String> queryUserGroupsIdList(String userId) {
        return userGroupDao.queryUserGroupsIdList(userId);
    }

    @Override
    public List<String> queryTalkBoxGroupsIdList(String userId) {
        return talkBoxDao.queryTalkBoxGroupsIdList(userId);
    }

    @Override
    public void appendFileRecord(FileRecordInfo fileRecordInfo) {
        FileRecord fileRecord = new FileRecord();
        fileRecord.setUserId(fileRecordInfo.getUserId());
        fileRecord.setFriendId(fileRecordInfo.getFriendId());
        fileRecord.setFileMD5(fileRecordInfo.getFileMD5());
        fileRecord.setClientFilePath(fileRecordInfo.getClientFilePath());
        fileRecord.setIsDelete(fileRecordInfo.getIsDelete());
        fileRecord.setFilePath(fileRecordInfo.getFilePath());
        fileRecord.setFileSize(fileRecordInfo.getFileSize());
        fileRecordDao.appendFileRecord(fileRecord);
    }

    @Override
    public FileRecordInfo queryFileRecord(String md5) {
        FileRecord fileRecord = fileRecordDao.queryFileRecord(md5);
        if (null == fileRecord) return null;
        FileRecordInfo fileRecordInfo = new FileRecordInfo();
        fileRecordInfo.setUserId(fileRecord.getUserId());
        fileRecordInfo.setFriendId(fileRecord.getFriendId());
        fileRecordInfo.setFileMD5(fileRecord.getFileMD5());
        fileRecordInfo.setClientFilePath(fileRecord.getClientFilePath());
        fileRecordInfo.setFilePath(fileRecord.getFilePath());
        fileRecordInfo.setFileSize(fileRecord.getFileSize());
        return fileRecordInfo;
    }

}
