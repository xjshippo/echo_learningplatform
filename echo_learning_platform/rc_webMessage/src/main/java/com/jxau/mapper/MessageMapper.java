package com.jxau.mapper;

import com.jxau.pojo.UserMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {

    List<UserMessage> selectMessagesByType(@Param("userId") String userId, @Param("type") String type);
}
