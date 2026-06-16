package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EssayCommentVO {
    private String id;
    private String parentId;
    private String invitationId;
    private String content;
    private Integer likeNumber;
    private Integer visitNumber;// 访问人数
    private Integer commentNumber;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
    private Integer state;
    private String userName;
    private String avatarUrl;
}
