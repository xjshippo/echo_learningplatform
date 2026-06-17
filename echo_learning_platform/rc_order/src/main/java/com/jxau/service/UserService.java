package com.jxau.service;

import com.jxau.annotations.Authorize;
import com.jxau.annotations.SystemControllerLog;
import com.jxau.pojo.*;
import com.jxau.util.ResultEntity;

import java.beans.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "rc-personal-service",contextId = "user")
@Service
public interface UserService {

    @PostMapping(value = "/wechat/login", consumes = "application/json")
    public ResultEntity wxLogin(@RequestBody HashMap map);

    @PostMapping(value = "/wechat/user/login", consumes = "application/json")
    public ResultEntity wxUserLogin(@RequestBody HashMap map);

    @GetMapping("/wechat/user/login/phone/code")
    public ResultEntity<String> wxUserPoneCode(@RequestParam("phone") String phone);

    @GetMapping("/wechat/user/login/phone")
    public ResultEntity wxUserLoginPhone(@RequestParam("userPhone") String userPhone,@RequestParam("code") String code);

    @PostMapping(value = "/learnTree/user/register", consumes = "application/json")
    public ResultEntity<LoginVO> userRegister(@RequestBody HashMap map) ;

    @GetMapping("/user/get/history/view")
    public ResultEntity<List<HistoryViewVO>> getUserHistoryView();

    @PostMapping(value = "/user/complete/information", consumes = "application/json")
    public ResultEntity<String> userCompleteInformation(@RequestBody HashMap map);

    @GetMapping("/user/get/Invitation")
    public ResultEntity<List<EssayUserVO>> getUserInvitation();

    @GetMapping("/user/get/comments")
    public ResultEntity<List<CommentPO>> getUserComments();

    @GetMapping("/get/user/by/id")
    ResultEntity<UserPO> getUserByUserId(@RequestParam("id") String id);

    @GetMapping("/user/get/information")
    public ResultEntity<UserInfoPO> userGetInfomation();

    @GetMapping("/token/get/user")
    public ResultEntity<UserPO> getTokenUser();

    @GetMapping("/get/user/all/professional")
    public ResultEntity<List<UserProfessionalVO>> getUsersProfessional();

    @PostMapping(value = "/user/studyTime/update", consumes = "application/json")
    ResultEntity<String> updateStudyTime(@RequestBody HashMap<String, String> map);

    @GetMapping("/user/studyTime/get")
    ResultEntity<HashMap<String, Object>> getStudyTime();

    @GetMapping("/user/favorites")
    ResultEntity<List<FavoritesPO>> getUserFavorites();

    @PostMapping(value = "/pay/vip/purchase", consumes = "application/json")
    ResultEntity<String> purchaseVip(@RequestBody HashMap<String, String> map);

    @PostMapping(value = "/pay/callback", consumes = "application/json")
    ResultEntity<String> payCallback(@RequestBody HashMap<String, String> map);

}
