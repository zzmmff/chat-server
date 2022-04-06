package org.itstack.naive.chat.protocol.talk;

import org.itstack.naive.chat.protocol.Command;
import org.itstack.naive.chat.protocol.Packet;

/**
 * 删除对话框请求
 */
public class DelTalkRequest extends Packet {

    private String userId;  // 用户ID
    private String talkId;  // 对话框ID

    public DelTalkRequest() {
    }

    public DelTalkRequest(String userId, String talkId) {
        this.userId = userId;
        this.talkId = talkId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    @Override
    public Byte getCommand() {
        return Command.DelTalkRequest;
    }
}
