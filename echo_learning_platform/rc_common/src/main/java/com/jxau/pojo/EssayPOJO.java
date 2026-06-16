package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author cheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EssayPOJO {

    private String id;
    private String title;
    private String content;
    private String userImgUrl;// 作者的头像
    private String userName;// 作者的姓名
    private String label;// 标签的集合
    private Integer state;
    private Integer likeNumber;
    private Integer visitNumber;
    private Integer commentNumber;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;
    private String coverImgUrl;



}
