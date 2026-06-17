package com.jxau.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxau.mapper.UserMapper;
import com.jxau.pojo.*;
import com.jxau.service.FavoritesService;
import com.jxau.service.LogService;
import com.jxau.service.UserService;
import com.jxau.util.AuthUtil;

import com.jxau.util.PhoneCodeUtil;
import com.jxau.util.RedisClient;
import com.jxau.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    public static String session_key = "session_key";
    public static String openid = "openid";

    @Value("${wx.AppId}")
    private String AppId;

    @Value("${user.imgUrl}")
    private String userNewImgUrl;

    @Value("${wx.AppSercet}")
    private String AppSercet;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LogService logService;

    @Resource
    private Token token;

    @Resource
    private RedisClient redisClient;

    @Resource
    private FavoritesService favoritesService;

    MD5 instance = MD5.create();

    @Override
    public ResultEntity wechatLogin(WeChatVO weChatVO) {


        UserPO user = null;

        try{
            String urlFormat = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
            String url = String.format(urlFormat,AppId, AppSercet, weChatVO.getCode());
            JSONObject jsonObject = AuthUtil.doGetJson(url);
            Map map = jsonObject.parseObject(JSONObject.toJSONString(jsonObject), Map.class);

            // 检查微信API是否返回错误
            if (map.get("errcode") != null) {
                return ResultEntity.falseWithoutData("微信登录失败: " + map.get("errmsg"));
            }
            // 从json字符串中取出session_key和openId
            Object sk = map.get(session_key);
            Object oi = map.get(openid);
            if (sk == null || oi == null) {
                return ResultEntity.falseWithoutData("微信登录失败: 未获取到openid");
            }
            String session_key_str = sk.toString();
            String openid_str = oi.toString();

            /*
             *根据open_id去数据库中查询是否出现该用户，不存在就说明是第一次登录，新建用户
             *如果存在，就不是第一次登录，更新最后登录时间
             * 保存登录日志
             * */

            user = userMapper.getUserByOpenId(openid_str);

            if(user == null || user.getId() == null)
            {
                // 用户第一次登录，初始化用户信息
                user= new UserPO();
                user.setLoginAct(weChatVO.getNickName());
                user.setNickName(weChatVO.getNickName());
                user.setImageUrl(weChatVO.getAvatarUrl());
                user.setOpenId(openid_str);
                user.setState(1);
                // 无密码暂时使用openId
                MD5 md5 = MD5.create();
                String s1 = md5.digestHex(openid_str);
                user.setPassWord(s1);
                String s = UUID.randomUUID().toString().replace("-","");
                user.setId(s);

                user.setLastTime(new Date());
                userMapper.insertUser(user);


                // 添加默认信息(手机号注册那也要)
                UserInfoPO userInfoPO = new UserInfoPO();
                userInfoPO.setId(UUID.randomUUID().toString().replace("-",""));
                userInfoPO.setUserId(s);
                userInfoPO.setProfessional("学生");
                userMapper.insertDefaultInfo(userInfoPO);

            }else{
                // 不是第一次登录，更新登录时间
                user.setLastTime(new Date());
                userMapper.updateLastTimeSelective(user);
            }

           /* // 更新日志
            LogPO logPO = new LogPO();
            logPO.setId(UUID.randomUUID().toString().replace("-",""));
            logPO.setCreateTime(new Date());
            logPO.setUserId(user.getId());

            logService.logSave(logPO);*/

            if(user == null) {
                // 登陆失败
                return ResultEntity.falseWithoutData("登录异常！");
            }
            else{
                // 下发token
                String token = this.token.getToken(user);
                Map map1 = new HashMap();
                map1.put("token",token);
                map1.put("user",user);
                return ResultEntity.successWithData(map1);

            }

        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }


    @Override
    public ResultEntity wechatUserLogin(HashMap map) {

        String loginAct =(String) map.get("loginAct");
        String passWord =(String) map.get("password");
        // 登录逻辑：通过唯一的账号名从数据库中查询出该用户，再比对密码,验证通过后判断用户的状态
        try{
            UserPO user = userMapper.getUserByLoginAct(loginAct);
            if(user != null)
            {
                // 比对密码

                String md5String = instance.digestHex(passWord);
                if (!md5String.equals(user.getPassWord()))
                    return ResultEntity.falseWithoutData("用户密码错误！");

                // 账号密码正确,判断用户状态
                //if("0".equals(user.getState()) || user.getOpenId() == null)
                if("0".equals(user.getState()))
                    return ResultEntity.falseWithoutData("用户状态异常！");

                // 登录成功，更新用户登录时间，并记录日志

                // 更新登录时间
                user.setLastTime(new Date());
                userMapper.updateLastTimeSelective(user);

                // 更新日志
                /*LogPO logPO = new LogPO();
                logPO.setId(UUID.randomUUID().toString().replace("-",""));
                logPO.setCreateTime(new Date());
                logPO.setUserId(user.getId());
                logPO.setContent("用户登录");
                logService.logSave(logPO);*/

                //下发token

                String token = this.token.getToken(user);
                Map map1 = new HashMap();
                map1.put("token",token);
                map1.put("user",user);
                return ResultEntity.successWithData(map1);

            }else {
                return ResultEntity.falseWithoutData("用户账号不存在！");
            }
        }catch (Exception e)
        {
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity<UserPO> getUserByUserId(String id) {
        try{
            UserPO userPO = userMapper.getUserByUserId(id);
            if(userPO != null) return ResultEntity.successWithData(userPO);
            else return ResultEntity.falseWithoutData("未查询到该用户信息！");
        }
        catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> wechatUserPhoneForCode(String userPhone) {


        try{
/*            // 检查数据库中是否存有对应的号码，没有则返回
            int count = userMapper.selectPhone(userPhone);
            if(count != 1) return ResultEntity.falseWithoutData("用户输入的号码不存在！请检查手机号码或者进行注册！");*/
            //  先检查发了没有，没发的话再发,判断数据库中是否有对应的key
            /*boolean b = redisClient.hasKey(userPhone);
            if(!b) return ResultEntity.falseWithoutData("请不要重复获取验证码！");*/

            // 发送验证码，k-v存入redis中
            ResultEntity<String> stringResultEntity = PhoneCodeUtil.SendCode(userPhone);
            if(ResultEntity.SUCCESS.equals(stringResultEntity.getResult()))
            {
                 //取出验证码，组成k-v键值对，存入redis中
                String code = stringResultEntity.getData();
                redisClient.set(userPhone, code, 5);

            }

            return ResultEntity.successWithoutData();
        }catch (Exception e)
        {
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity wechatLoginByPhone(String userPhone, String code) {

        /*
        * 逻辑：以userphone为key，去redis中取出value与code进行对比，对比成功则下发token，更新用户登录状态，记录日志
        * */

        try{

            // 先判断手机号在不在数据库中，不在的话直接注册一个用户存入数据库中
            int i = userMapper.selectPhone(userPhone);
            if(i == 0){
                // 说明数据库中没有手机号，创建新用户放入数据库中
                UserPO userPO = new UserPO();
                String userId = UUID.randomUUID().toString().replace("-","");
                userPO.setId(userId);
                userPO.setState(1);
                userPO.setLoginAct(userPhone);
                String s = instance.digestHex(userPhone);
                userPO.setPassWord(s);
                userPO.setImageUrl(userNewImgUrl);
                userPO.setLastTime(new Date());
                userPO.setNickName(userPhone);
                userPO.setPhone(userPhone);
                userMapper.insertUser(userPO);

                // 添加默认收藏夹
                favoritesService.createFavorites("默认收藏夹",userPO.getId());

                // 添加默认信息(手机号注册那也要)
                UserInfoPO userInfoPO = new UserInfoPO();
                userInfoPO.setId(UUID.randomUUID().toString().replace("-",""));
                userInfoPO.setUserId(userId);
                userInfoPO.setPhone(userPhone);
                userInfoPO.setProfessional("学生");
                userMapper.insertDefaultInfo(userInfoPO);


                //下发token
                String token = this.token.getToken(userPO);
                Map map1 = new HashMap();
                map1.put("token",token);
                map1.put("user",userPO);
                // 清空验证码
                redisClient.del(userPhone);
                return ResultEntity.successWithData(map1);
            }


            String o =(String) redisClient.get(userPhone);

            if(o == null || o.length() == 0) return ResultEntity.falseWithoutData("验证码已过期！");

            if(!code.equals(o)) return ResultEntity.falseWithoutData("验证码错误");

            // 登陆成功，清空验证码，更新用户状态

            // 根据phone查询出user
            UserPO user = userMapper.getUserByPhone(userPhone);

            if(user == null) return ResultEntity.falseWithoutData("用户数据为空！");

            // 更新登录时间
            user.setLastTime(new Date());
            userMapper.updateLastTimeSelective(user);

           /* // 更新日志
            LogPO logPO = new LogPO();
            logPO.setId(UUID.randomUUID().toString().replace("-",""));
            logPO.setCreateTime(new Date());
            logPO.setUserId(user.getId());
            logPO.setContent("用户登录");
            logService.logSave(logPO);*/

            //下发token
            String token = this.token.getToken(user);
            Map map1 = new HashMap();
            map1.put("token",token);
            map1.put("user",user);
            // 清空验证码
            redisClient.del(userPhone);
            return ResultEntity.successWithData(map1);

        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity<List<HistoryViewVO>> getUserHistoryView(String currentUserId) {
        try {
            List<HistoryViewVO> list = userMapper.getHistoryView(currentUserId);
            return ResultEntity.successWithData(list);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultEntity<LoginVO> saveUser(UserPO userPO) {

        try{

//            // 验证验证码是否正确
//            String phone = userPO.getPhone();
//            String rcode1 =(String) redisClient.get(phone);
//            String rcode = String.valueOf(rcode1);
//            if(rcode == null || rcode.length() == 0) return ResultEntity.falseWithoutData("验证码已过期！");
//            if(!code.equals(rcode)) return ResultEntity.falseWithoutData("验证码错误");

            // 注册成功，清空验证码，更新用户状态


            // 密码加密
            String passWord = userPO.getPassWord();
            MD5 md5 = MD5.create();
            String s = md5.digestHex(passWord);
            userPO.setPassWord(s);
            if (userPO.getNickName() == null || userPO.getNickName().isEmpty()) {
                userPO.setNickName(userPO.getLoginAct());
            }
            userPO.setImageUrl(userNewImgUrl);
            userPO.setState(1);
            userPO.setLastTime(new Date());
            String userId = UUID.randomUUID().toString().replace("-","");
            userPO.setId(userId);
            userMapper.insertUser(userPO);

            /*// 更新日志
            LogPO logPO = new LogPO();
            logPO.setId(UUID.randomUUID().toString().replace("-",""));
            logPO.setCreateTime(new Date());
            logPO.setUserId(userPO.getId());
            logPO.setContent("用户注册");
            logService.logSave(logPO);*/



            // 添加默认收藏夹
            favoritesService.createFavorites("默认收藏夹",userPO.getId());

            // 添加默认信息(手机号注册那也要)
            UserInfoPO userInfoPO = new UserInfoPO();
            userInfoPO.setId(UUID.randomUUID().toString().replace("-",""));
            userInfoPO.setUserId(userId);

            userMapper.insertDefaultInfo(userInfoPO);

            // 给定默认职业
            UserVO userVO = new UserVO();
            userVO.setUserId(userPO.getId());
            userVO.setProfessional("学生");
            userMapper.updateUserInformation(userVO);

            // 清空验证码
            //redisClient.del(phone);

            //下发token
            String token = this.token.getToken(userPO);
            Map map1 = new HashMap();
            map1.put("token",token);
            map1.put("user",userPO);
            LoginVO loginVO = new LoginVO(token, userPO);
            return ResultEntity.successWithData(loginVO);

        }
        catch (Exception e){
            if(e instanceof org.springframework.dao.DuplicateKeyException){
                return ResultEntity.falseWithoutData("用户名已存在");
            }
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity<String> updateInformation(UserVO userVO) {
        try{
            // 更新用户的信息
            userMapper.updateUserInformation(userVO);

//            // 更新日志
//            LogPO logPO = new LogPO();
//            logPO.setId(UUID.randomUUID().toString().replace("-",""));
//            logPO.setCreateTime(new Date());
//            logPO.setUserId(userVO.getUserId());
//            logPO.setContent("用户修改信息");
//            logService.logSave(logPO);

            return ResultEntity.successWithoutData();

        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<HistoryViewVO>> getUserInvitation(String currentUserId) {

        try{
            List<HistoryViewVO> list = userMapper.selectInvitationByUserId(currentUserId);
            return ResultEntity.successWithData(list);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<CommentPO>> getUserComments(String currentUserId) {
        try{
            List<CommentPO> list = userMapper.selectCommentsByUserId(currentUserId);
            return ResultEntity.successWithData(list);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity<UserPO> getUserByToken() {
        try{
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            UserPO user =(UserPO) request.getAttribute("user");
            if(user == null){
                return ResultEntity.falseWithoutData("用户不存在！未查询到改用户！",String.valueOf(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value()));
            }
            return ResultEntity.successWithData(user);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<UserInfoPO> getUserInfo(String currentUserId) {
        try{
            // 根据用户id去数据库中查询出对应的基本信息
            UserInfoPO userInfoPO = userMapper.selectUserInfo(currentUserId);

            // 查询出所有的可选职业,并放入到用户基本信息中
            List<String> list = userMapper.selectAllProfessionals();
            userInfoPO.setProfessionals(list);

            if(userInfoPO == null) return ResultEntity.falseWithoutData("为查询到数据，请联系管理员");
            return ResultEntity.successWithData(userInfoPO);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<UserProfessionalVO>> getUserProfessional() {
        try{
            List<UserProfessionalVO> list = userMapper.selectUserProfessional();
            for (UserProfessionalVO userProfessionalVO : list) {
                List<String> likeInvitationIds = userMapper.selectLikeInvitationIds(userProfessionalVO.getUserId());
                userProfessionalVO.setInvitationId(likeInvitationIds);
            }
            return ResultEntity.successWithData(list);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<EssayUserVO>> getUserInvitations(String currentUserId) {
        try{

            List<EssayUserVO> essays = userMapper.selectUserAllEssays(currentUserId);
            for (EssayUserVO e: essays) {
                e.setTag(e.getTags().split(","));
            }
            return ResultEntity.successWithData(essays);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getTest(String userId) {
        UserPO userByUserId = userMapper.getUserByUserId(userId);
        return this.token.getToken(userByUserId);
    }

    @Override
    public ResultEntity<String> updateStudyTime(String currentUserId, String addMinutes) {
        try {
            int minutes = addMinutes != null ? Integer.parseInt(addMinutes) : 0;
            userMapper.updateStudyTime(currentUserId, minutes);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<HashMap<String, Object>> getStudyTime(String currentUserId) {
        try {
            HashMap<String, Object> result = userMapper.selectStudyTime(currentUserId);
            return ResultEntity.successWithData(result);
        } catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<FavoritesPO>> getUserFavorites(String currentUserId) {
        try {
            List<FavoritesPO> list = userMapper.selectUserFavorites(currentUserId);
            return ResultEntity.successWithData(list);
        } catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> purchaseVip(String currentUserId, String months) {
        try {
            int m = months != null ? Integer.parseInt(months) : 1;
            userMapper.updateUserVip(currentUserId, m);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> payCallback(HashMap<String, String> map) {
        try {
            String userId = map.get("userId");
            String months = map.getOrDefault("months", "1");
            int m = Integer.parseInt(months);
            userMapper.updateUserVip(userId, m);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
}
