package com.jxau.mapper;

import com.jxau.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    UserPO getUserByUserId(@Param("id")String id);

    List<UserProfessionalVO> selectUserProfessional();

    List<String> selectLikeInvitationIds(String userId);
}
