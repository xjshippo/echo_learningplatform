package com.jxau.mapper;

import com.jxau.pojo.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserPO getUserByUserId(@Param("id")String id);
}
