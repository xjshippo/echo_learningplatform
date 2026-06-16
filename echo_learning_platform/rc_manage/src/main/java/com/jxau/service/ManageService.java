package com.jxau.service;

import com.github.pagehelper.PageInfo;
import com.jxau.pojo.*;
import com.jxau.util.ResultEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


public interface ManageService {


    ResultEntity<PageInfo<EssayPO>> selectAllEssays(HashMap<String, String> map);

    ResultEntity<String> audirEssays(HashMap<String, String> map);

    ResultEntity<String> deleteEssays(HashMap<String, String> map);

    ResultEntity<List<UserInfoVO>> getAllUsers(String state);

    ResultEntity<String> audirUsers(HashMap<String, String> map);

    ResultEntity<String> deleteUser(HashMap<String, String> map);
}
