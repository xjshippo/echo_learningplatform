package com.jxau.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussCommentVO {
    private String id;
    private String author;
    private String src;
    private String title;
    private String content;
    private ArrayList<String> contentImg;
    private Integer likeNum;
    private Integer collectNum;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date create_time;
}
