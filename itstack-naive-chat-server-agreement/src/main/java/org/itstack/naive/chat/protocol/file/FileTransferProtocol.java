package org.itstack.naive.chat.protocol.file;

import org.itstack.naive.chat.protocol.Command;
import org.itstack.naive.chat.protocol.Packet;

/**
 * @ClassName FileTransferProtocol
 * @Description 文件传输协议
 * @Author 张慕帆
 * @Date 1:20 PM 4/9/2022
 * @Version 1.0
 **/
public class FileTransferProtocol extends Packet {

    private Integer transferType; //0请求传输文件、1文件传输指令、2文件传输数据
    private Object transferObj;   //数据对象；(0)FileDescInfo、(1)FileBurstInstruct、(2)FileBurstData

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public Object getTransferObj() {
        return transferObj;
    }

    public void setTransferObj(Object transferObj) {
        this.transferObj = transferObj;
    }

    /**
     * 获取协议指令
     *
     * @return 返回指令值
     */
    @Override
    public Byte getCommand() {
        return Command.FileTransfer;
    }
}
