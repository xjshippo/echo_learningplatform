package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

/**
 * 讨论区，讨论表
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussCommentPO {
    private String id;
    private String title;
    private String content;
    private String discussId;
    private String userId;
    private UserPO user;
    private String parentId;
    private Integer likeNumber;
    private Integer visitNumber;
    private Integer commentNumber;
    private Date create_time;
    private Integer state;
    private Integer collectNum;
    private ArrayList<ImagePo> imagePos;
}
