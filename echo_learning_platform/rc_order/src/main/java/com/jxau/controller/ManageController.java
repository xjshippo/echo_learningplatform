package com.jxau.controller;

import com.github.pagehelper.PageInfo;
import com.jxau.pojo.EssayPO;
import com.jxau.pojo.UserInfoVO;
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
        try{
            return  manageService.selectAuditEssay(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    /**
     * 审核文章
     * @param map
     * @return
     */
    @PostMapping("/manage/essays/audit")
    @CrossOrigin// 解决跨域问题
    public ResultEntity<String> auditEssays(@RequestBody HashMap<String,String> map){
        try{
            return  manageService.auditEssays(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    /**
     * 删除文章
     * @param map
     * @return
     */
    @PostMapping("/manage/essays/delete")
    @CrossOrigin// 解决跨域问题
    public ResultEntity<String> deleteEssays(@RequestBody HashMap<String,String> map){
        try{
            return  manageService.deleteEssays(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    /**
     *查看所有需要审核的用户(带上state = "0")
     * 查看所有用户(带上state = "")
     */
    @GetMapping("/user/get/all/audit")
    public ResultEntity<List<UserInfoVO>> getAllUser(@RequestParam(value = "state",defaultValue = "")String state){
        try{
            return  manageService.getAllUser(state);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    /**
     * 审核用户
     * @param map
     * @return
     */
    @PostMapping("/manage/user/audit")
    public ResultEntity<String> auditUsers(@RequestBody HashMap<String,String> map){
        try{
            return  manageService.auditUsers(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    /**
     * 删除用户
     * @param map
     * @return
     */
    @PostMapping("/manage/users/delete")
    public ResultEntity<String> deleteUser(@RequestBody HashMap<String,String> map){
        try{
            return  manageService.deleteUser(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }


}
