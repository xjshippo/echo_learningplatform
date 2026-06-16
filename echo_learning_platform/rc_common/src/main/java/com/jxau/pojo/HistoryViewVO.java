package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryViewVO {

    // 返回浏览历史的文章
    private String id;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;
    private String imgUrl;
    private String title;
    private String userImgUrl;// 作者的头像
    private String userName;// 作者的姓名
    private String label;// 标签的集合
    private Integer likeNumber;// 点赞数
    private Integer visitNumber;// 访问数
    private Integer commentNumber;// 评论数
}
