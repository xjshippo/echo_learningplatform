package com.jxau.mapper;

import com.jxau.pojo.LogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogMapper {

    void insert(LogPO logPO);
}
