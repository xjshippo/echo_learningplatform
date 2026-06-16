package com.jxau.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户社区中间表
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussVO {
    private String id;
    private String uId;
    private String dId;
    private DiscussPO discussPO;
}
