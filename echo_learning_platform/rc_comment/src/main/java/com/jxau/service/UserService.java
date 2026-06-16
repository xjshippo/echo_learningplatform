package com.jxau.service;

import com.jxau.pojo.UserPO;
import com.jxau.util.ResultEntity;

public interface UserService {

    ResultEntity<UserPO> getUserByUserId(String id);

}
