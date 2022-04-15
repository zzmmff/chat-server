package org.itstack.naive.chat.protocol.file;

import org.itstack.naive.chat.protocol.Command;
import org.itstack.naive.chat.protocol.Packet;

/**
 * @ClassName DownLoadFileRequest
 * @Description TODO
 * @Author 张慕帆
 * @Date 9:01 PM 4/10/2022
 * @Version 1.0
 **/
public class DownLoadFileRequest extends Packet {

    private Integer transferType;    //0 文件信息描述 、1 文件分片信息 、2 文件分片数据  3 md5

    private Object transferObj;      //(0) FileDescInfo 、 (1) FileBurstInstruct 、 (2) FileBurstData (3) String


    /**
     * 获取协议指令
     *
     * @return 返回指令值
     */
    @Override
    public Byte getCommand() {
        return Command.DownLoadFile;
    }

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
}
