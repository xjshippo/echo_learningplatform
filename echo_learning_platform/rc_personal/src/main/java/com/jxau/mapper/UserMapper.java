package com.jxau.mapper;

import com.jxau.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {

    // 根据open_id，查询用户
    UserPO getUserByOpenId(@Param("openId") String openId);

    void insertUser(UserPO userPO);

    void updateLastTimeSelective(UserPO user);

    UserPO getUserByLoginAct(@Param("loginAct")String loginAct);

    UserPO getUserByUserId(@Param("id")String id);

    int selectPhone(@Param("userPhone") String userPhone);

    UserPO getUserByPhone(@Param("userPhone")String userPhone);

    List<HistoryViewVO> getHistoryView(@Param("currentUserId")String currentUserId);

    void updateUserInformation(UserVO userVO);

    List<HistoryViewVO> selectInvitationByUserId(@Param("currentUserId")String currentUserId);

    List<CommentPO> selectCommentsByUserId(@Param("currentUserId")String currentUserId);

    UserInfoPO selectUserInfo(@Param("currentUserId")String currentUserId);

    List<String> selectAllProfessionals();

    void insertDefaultInfo(UserInfoPO userInfoPO);

    List<UserProfessionalVO> selectUserProfessional();

    List<String> selectLikeInvitationIds(@Param("userId")String userId);

    List<EssayUserVO> selectUserAllEssays(@Param("currentUserId")String currentUserId);
}
