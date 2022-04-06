package org.itstack.naive.chat.protocol.friend;

import org.itstack.naive.chat.protocol.Command;
import org.itstack.naive.chat.protocol.Packet;
import org.itstack.naive.chat.protocol.friend.dto.UserDto;

import java.util.List;

/**
 * 搜索好友应答
 */
public class SearchFriendResponse extends Packet {

    private List<UserDto> list;

    public List<UserDto> getList() {
        return list;
    }

    public void setList(List<UserDto> list) {
        this.list = list;
    }

    @Override
    public Byte getCommand() {
        return Command.SearchFriendResponse;
    }
}
