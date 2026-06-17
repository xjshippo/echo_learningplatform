package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {

    private String id;
    private String loginAct;
    private String passWord;
    private String nickName;
    private Integer state;
    private String imageUrl;
    private String phone;
    private String openId;
    private Date lastTime;
    private Integer isVip;
    private Date vipExpireTime;
    private Integer studyTimeToday;
    private Integer studyTimeWeek;
}
