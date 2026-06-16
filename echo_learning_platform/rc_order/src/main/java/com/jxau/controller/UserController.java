package com.jxau.controller;

import com.alibaba.fastjson.JSON;
import com.jxau.annotations.Authorize;
import com.jxau.annotations.SystemControllerLog;
import com.jxau.pojo.*;
import com.jxau.service.UserService;
import com.jxau.util.ResultEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/wechat/login")
    public ResultEntity wxLogin(@RequestBody HashMap map) {
        try{
            return userService.wxLogin(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }


   /* @GetMapping("/wechat/user/login")
    public ResultEntity wxUserLogin(@RequestParam HashMap map){
        try{
            return userService.wxUserLogin(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }*/

    @PostMapping("/wechat/user/login")
    public ResultEntity wxUserLogin(@RequestBody HashMap<String,String> map){
        try{
            return userService.wxUserLogin(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }


    @GetMapping("/wechat/user/login/phone/code")
    public ResultEntity<String> wxUserPoneCode(@RequestParam("phone") String phone){
        try{
            return userService.wxUserPoneCode(phone);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @GetMapping("/wechat/user/login/phone")
    public ResultEntity wxUserLoginPhone(@RequestParam("userPhone") String userPhone,@RequestParam("code") String code){
        try{
            return userService.wxUserLoginPhone(userPhone,code);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/learnTree/user/register")
    public ResultEntity<LoginVO> userRegister(@RequestBody HashMap map){
        try{
            return userService.userRegister(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/user/get/history/view")
    @Authorize
    public ResultEntity<List<HistoryViewVO>> getUserHistoryView(){
        try{
            return userService.getUserHistoryView();
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/user/complete/information")
    @Authorize
    public ResultEntity<String> userCompleteInformation(@RequestBody HashMap map){
        try{
            return userService.userCompleteInformation(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/user/get/Invitation")
    @Authorize
    public ResultEntity<List<EssayUserVO>> getUserInvitation(){
        try{
            return userService.getUserInvitation();
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/user/get/comments")
    @Authorize
    public ResultEntity<List<CommentPO>> getUserComments(){
        try{
            return userService.getUserComments();
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/get/user/by/id")
    public ResultEntity<UserPO> getUserByUserId(@RequestParam("id") String id){
        try{
            return userService.getUserByUserId(id);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/user/get/information")
    @Authorize
    public ResultEntity<UserInfoPO> userGetInfomation(){
        try{
            ResultEntity<UserInfoPO> userInfoPOResultEntity = userService.userGetInfomation();
            return userInfoPOResultEntity;
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/token/get/user")
    @Authorize
    public ResultEntity<UserPO> getTokenUser(){
        try{
            return userService.getTokenUser();
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/get/user/all/professional")
    public ResultEntity<List<UserProfessionalVO>> getUsersProfessional(){
        try{
            return userService.getUsersProfessional();
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }



}
