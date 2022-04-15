package org.itstack.naive.chat.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.itstack.naive.chat.infrastructure.po.FileRecord;

@Mapper
public interface IFileRecordDao {
    void appendFileRecord(FileRecord record);

    FileRecord queryFileRecord(String md5);
}
