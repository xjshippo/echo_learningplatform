package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户中间表
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LookUserPo {
    private String id;
    private String uid;
    private String vid;
    private UserPO user;
}
