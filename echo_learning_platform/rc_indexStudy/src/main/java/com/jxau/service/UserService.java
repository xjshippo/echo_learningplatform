package com.jxau.service;

import com.jxau.pojo.*;
import com.jxau.util.ResultEntity;

import java.util.List;

public interface UserService {

    ResultEntity<UserPO> getUserByUserId(String id);

    ResultEntity<List<UserProfessionalVO>> getUserProfessional();
}
