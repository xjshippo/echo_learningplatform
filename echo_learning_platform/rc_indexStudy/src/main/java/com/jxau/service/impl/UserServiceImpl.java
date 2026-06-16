package com.jxau.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONObject;
import com.jxau.mapper.UserMapper;
import com.jxau.pojo.*;
import com.jxau.service.UserService;
import com.jxau.util.AuthUtil;
import com.jxau.util.ResultEntity;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

/**
 * @author daitao
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private Token token;

    MD5 instance = MD5.create();


    @Override
    public ResultEntity<UserPO> getUserByUserId(String id) {
        try{
            UserPO userPO = userMapper.getUserByUserId(id);
            if(userPO != null) return ResultEntity.successWithData(userPO);
            else return ResultEntity.falseWithoutData("未查询到该用户信息！");
        }
        catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<UserProfessionalVO>> getUserProfessional() {
        try{
            List<UserProfessionalVO> list = userMapper.selectUserProfessional();
            for (UserProfessionalVO userProfessionalVO : list) {
                List<String> likeInvitationIds = userMapper.selectLikeInvitationIds(userProfessionalVO.getUserId());
                userProfessionalVO.setInvitationId(likeInvitationIds);
            }
            return ResultEntity.successWithData(list);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
}
