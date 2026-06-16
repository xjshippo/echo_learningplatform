package com.jxau.service;

import com.jxau.pojo.UserPO;
import com.jxau.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface UserService {

     @GetMapping("/get/user/by/id")
     ResultEntity<UserPO> getUserByUserId(@RequestParam String id);
}
