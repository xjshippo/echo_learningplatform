package com.jxau.service;

import com.github.pagehelper.PageInfo;
import com.jxau.pojo.EssayPO;
import com.jxau.pojo.UserInfoVO;
import com.jxau.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient("rc-manage-service")
@Service
public interface ManageService {

    @PostMapping(value = "/get/all/audit/essays", consumes = "application/json")
    public ResultEntity<PageInfo<EssayPO>> selectAuditEssay(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/manage/essays/audit",consumes = "application/json")
    public ResultEntity<String> auditEssays(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/manage/essays/delete",consumes = "application/json")
    public ResultEntity<String> deleteEssays(@RequestBody HashMap<String,String> map);

    @GetMapping("/user/get/all/audit")
    public ResultEntity<List<UserInfoVO>> getAllUser(@RequestParam(value = "state",defaultValue = "")String state);

    @PostMapping(value = "/manage/user/audit", consumes = "application/json")
    public ResultEntity<String> auditUsers(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/manage/users/delete", consumes = "application/json")
    public ResultEntity<String> deleteUser(@RequestBody HashMap<String,String> map);
}
