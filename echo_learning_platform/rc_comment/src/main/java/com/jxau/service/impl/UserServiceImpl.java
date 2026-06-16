package com.jxau.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.jxau.mapper.UserMapper;
import com.jxau.pojo.UserPO;
import com.jxau.service.UserService;
import com.jxau.util.ResultEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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


}
