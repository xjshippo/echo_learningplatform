package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author chenzouquan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EssayVO {
    private String id;
    private String userName;
    private String avatarUrl;
    private String title;
    private String content;
    private ArrayList<String> images;
    private String[] tags;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
    private Integer likeNumber;
    private Integer visitNumber;
    private Integer commentNumber;
    private boolean thumbStatus;

    public EssayVO(String id, String userName, String avatarUrl, String title, String content, ArrayList<String> images, String[] tags, Date create_time, Integer likeNumber, Integer visitNumber,Integer commentNumber) {
        this.id = id;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.content = content;
        this.images = images;
        this.tags = tags;
        this.create_time = create_time;
        this.likeNumber = likeNumber;
        this.commentNumber = commentNumber;
        this.visitNumber = visitNumber;
    }
}
