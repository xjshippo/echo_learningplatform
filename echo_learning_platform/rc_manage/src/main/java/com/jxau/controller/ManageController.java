package com.jxau.controller;

import com.github.pagehelper.PageInfo;
import com.jxau.pojo.*;
import com.jxau.service.ManageService;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class ManageController {

    @Autowired
    private ManageService manageService;

    /**
     * 查看所有需要审核的文章(带上state = "0")
     * 查看所有文章(带上state = "")
     * @param map
     * @return
     */
    @PostMapping("/get/all/audit/essays")
    @CrossOrigin// 解决跨域问题
    public ResultEntity<PageInfo<EssayPO>> selectAuditEssay(@RequestBody HashMap<String,String> map){
        return  manageService.selectAllEssays(map);
    }

    /**
     * 审核文章
     * @param map
     * @return
     */
    @PostMapping("/manage/essays/audit")
    @CrossOrigin// 解决跨域问题
    public ResultEntity<String> auditEssays(@RequestBody HashMap<String,String> map){
        return manageService.audirEssays(map);
    }

    /**
     * 删除文章
     * @param map
     * @return
     */
    @PostMapping("/manage/essays/delete")
    @CrossOrigin// 解决跨域问题
    public ResultEntity<String> deleteEssays(@RequestBody HashMap<String,String> map){
        return manageService.deleteEssays(map);
    }

    /**
     *查看所有需要审核的用户(带上state = "0")
     * 查看所有用户(带上state = "")
     */
    @GetMapping("/user/get/all/audit")
    public ResultEntity<List<UserInfoVO>> getAllUser(@RequestParam(value = "state",defaultValue = "")String state){
        return manageService.getAllUsers(state);
    }

    /**
     * 审核用户
     * @param map
     * @return
     */
    @PostMapping("/manage/user/audit")
    public ResultEntity<String> auditUsers(@RequestBody HashMap<String,String> map){
        return manageService.audirUsers(map);
    }

    /**
     * 删除用户
     * @param map
     * @return
     */
    @PostMapping("/manage/users/delete")
    public ResultEntity<String> deleteUser(@RequestBody HashMap<String,String> map){
        return manageService.deleteUser(map);
    }


}
