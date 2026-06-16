package com.jxau.mapper;

import com.jxau.pojo.CommentPO;
import com.jxau.pojo.HistoryViewVO;
import com.jxau.pojo.UserPO;
import com.jxau.pojo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    UserPO getUserByUserId(@Param("id")String id);

}
