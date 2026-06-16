package com.jxau.service;

import com.jxau.pojo.*;
import com.jxau.util.JsonResult;
import com.jxau.util.ResultEntity;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    // 根据code 返回json字符串
    ResultEntity wechatLogin(WeChatVO weChatVO);

    ResultEntity wechatUserLogin(HashMap map);

    ResultEntity<UserPO> getUserByUserId(String id);

    ResultEntity<String> wechatUserPhoneForCode(String userPhone);

    ResultEntity wechatLoginByPhone(String userPhone, String code);

    ResultEntity<List<HistoryViewVO>> getUserHistoryView(String currentUserId);

    ResultEntity<LoginVO> saveUser(UserPO userPO);

    ResultEntity<String> updateInformation(UserVO userVO);

    ResultEntity<List<HistoryViewVO>> getUserInvitation(String currentUserId);

    ResultEntity<List<CommentPO>> getUserComments(String currentUserId);

    ResultEntity<UserPO> getUserByToken();

    ResultEntity<UserInfoPO> getUserInfo(String currentUserId);

    ResultEntity<List<UserProfessionalVO>> getUserProfessional();

    ResultEntity<List<EssayUserVO>> getUserInvitations(String currentUserId);

    String getTest(String userId);
}
