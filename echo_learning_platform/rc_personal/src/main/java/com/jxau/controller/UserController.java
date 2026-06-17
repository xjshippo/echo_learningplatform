package com.jxau.controller;

import com.alibaba.fastjson.JSON;
import com.jxau.annotations.Authorize;
import com.jxau.annotations.SystemControllerLog;
import com.jxau.pojo.*;
import com.jxau.service.UserService;
import com.jxau.service.impl.Token;
import com.jxau.util.ResultEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {


    @Resource
    private UserService userService;

    /**
     * 微信小程序一键登录
     * 登录成功后，将用户身份信息及session_key存入token
     *
     * @return
     */
    @PostMapping("/wechat/login")
    public ResultEntity wxLogin(@RequestBody HashMap map) {



        String code = (String) map.get("code");
        String nickName = (String) map.get("nickName");
        String avatarUrl = (String) map.get("avatarUrl");
        WeChatVO weChatVO = new WeChatVO(code,nickName,avatarUrl);
        // 通过code访问微信（腾讯）接口，微信（腾讯）接口返回当前登录的信息：session_key及openid
        // 返回的openid是每个用户唯一的，可以通过openid来判断这个人是谁
        return userService.wechatLogin(weChatVO);

    }

    /**
     * 使用账号密码进行登录
     * @param map
     * @return
     */
    @PostMapping("/wechat/user/login")
    public ResultEntity wxUserLogin(@RequestBody HashMap<String,String> map){

        return userService.wechatUserLogin(map);
    }

    /**
     * 根据手机号码发送验证码
     * @param phone
     * @return
     */
    @GetMapping("/wechat/user/login/phone/code")
    public ResultEntity<String> wxUserPoneCode(@RequestParam("phone") String phone)
    {
        return userService.wechatUserPhoneForCode(phone);
    }


    /**
     * 手机号登录
     * @param userPhone
     * @param code
     * @return
     */
    @GetMapping("/wechat/user/login/phone")
    public ResultEntity wxUserLoginPhone(@RequestParam("userPhone") String userPhone,@RequestParam("code") String code)
    {
        return userService.wechatLoginByPhone(userPhone,code);
    }


    /**
     * 用户注册功能
     * @param map
     * @return
     */
    @PostMapping("/learnTree/user/register")
    public ResultEntity<LoginVO> userRegister(@RequestBody HashMap map) {

        String loginAct = (String) map.get("userName");
        String passWord = (String) map.get("password");

        UserPO userPO = new UserPO();
        userPO.setLoginAct(loginAct);
        userPO.setPassWord(passWord);
        return userService.saveUser(userPO);
    }

    /**
     * 查看历史文章
     *
     * @return
     */
    @GetMapping("/user/get/history/view")
    @Authorize
    public ResultEntity<List<HistoryViewVO>> getUserHistoryView() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUser = request.getHeader("currentUser");
        //String currentUserId =(String) request.getHeader("currentUser");
        return userService.getUserHistoryView(currentUser);

    }


    /**
     * 获得用户个人信息
     * @return
     */
    @GetMapping("/user/get/information")
    public ResultEntity<UserInfoPO> userGetInfomation() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId =(String) request.getHeader("currentUser");
        return userService.getUserInfo(currentUserId);
    }


    /**
     * 用户信息完善
     *
     * @return
     */
    @PostMapping("/user/complete/information")
    public ResultEntity<String> userCompleteInformation(@RequestBody HashMap map) {


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId =(String) request.getHeader("currentUser");
        String sex = (String)map.get("sex");
        String address = (String)map.get("address");
        String phone = (String)map.get("phone");
        String QQ = (String)map.get("QQ");
        String email = (String)map.get("email");
        String professional = (String)map.get("professional");
        UserVO userVO = new UserVO();
        userVO.setUserId(currentUserId);
        userVO.setEmail(email);
        userVO.setAddress(address);
        userVO.setQQ(QQ);
        userVO.setPhone(phone);
        userVO.setSex(sex);
        userVO.setProfessional(professional);
        return userService.updateInformation(userVO);

    }

    /**
     * 获取我的博客
     * @return
     */
    @GetMapping("/user/get/Invitation")
    public ResultEntity<List<EssayUserVO>> getUserInvitation(){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String currentUserId =(String) request.getHeader("currentUser");
        return userService.getUserInvitations(currentUserId);

    }


    /**
     * 获取我的评论
     * @return
     */
    @GetMapping("/user/get/comments")
    public ResultEntity<List<CommentPO>> getUserComments(){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId =(String) request.getHeader("currentUser");
        return userService.getUserComments(currentUserId);

    }

    @GetMapping("/get/user/by/id")
    public ResultEntity<UserPO> getUserByUserId(@RequestParam("id") String id){
        return userService.getUserByUserId(id);
    }


    /**
     * 根据token返回用户信息
     * @return
     */
    @GetMapping("/token/get/user")
    @Authorize
    public ResultEntity<UserPO> getTokenUser(){

        return userService.getUserByToken();
    }


    @GetMapping("/get/user/all/professional")
    public ResultEntity<List<UserProfessionalVO>> getUsersProfessional(){
        return userService.getUserProfessional();
    }

    /**
     * 上报学习时长
     */
    @PostMapping("/user/studyTime/update")
    public ResultEntity<String> updateStudyTime(@RequestBody HashMap<String, String> map) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        String addMinutes = map.get("addMinutes");
        return userService.updateStudyTime(currentUserId, addMinutes);
    }

    /**
     * 获取学习时长
     */
    @GetMapping("/user/studyTime/get")
    public ResultEntity<HashMap<String, Object>> getStudyTime() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        return userService.getStudyTime(currentUserId);
    }

    /**
     * 获取我的收藏
     */
    @GetMapping("/user/favorites")
    public ResultEntity<List<FavoritesPO>> getUserFavorites() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        return userService.getUserFavorites(currentUserId);
    }

    /**
     * 购买VIP（模拟支付）
     */
    @PostMapping("/pay/vip/purchase")
    public ResultEntity<String> purchaseVip(@RequestBody HashMap<String, String> map) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        String months = map.getOrDefault("months", "1");
        return userService.purchaseVip(currentUserId, months);
    }

    /**
     * 微信支付回调（模拟）
     */
    @PostMapping("/pay/callback")
    public ResultEntity<String> payCallback(@RequestBody HashMap<String, String> map) {
        return userService.payCallback(map);
    }



}
