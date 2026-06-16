package com.jxau.mapper;

import com.jxau.pojo.Essay;
import com.jxau.pojo.EssayPO;
import com.jxau.pojo.UserInfoPO;
import com.jxau.pojo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface ManageMapper {

    List<EssayPO> selectAllEssays(@Param("state") String state);

    ArrayList<String> selectEssayImagesById(@Param("id") String id);

    void updateEssaysState(@Param("id") String id);

    void deleteEssays(@Param("id") String id);

    List<UserInfoVO> selectAllUser(@Param("state") String state);

    void updateUserState(@Param("userId")String userId);

    void deleteUser(@Param("id")String id);
}
