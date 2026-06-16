package com.jxau.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 资源类
 * @author chenzouquan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourcePO {
    private String id;
    private String content;
    private String uid;
    private UserPO user;
    /** 资源地址*/
    private String url;
    /** 封面地址*/
    private String imageUrl;
    /** 种类*/
    private String type;
    /** 标签*/
    private String tags;
    private String[] tag;
    private Date create_time;
    private Integer visitedNumber;
    private Integer likeNumber;
    private Integer commentNumber;
    private Integer state;
    private String size;

}
