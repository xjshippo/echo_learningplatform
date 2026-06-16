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
@AllArgsConstructor
@NoArgsConstructor
public class EssayPO {
    private String id;
    private String userName;
    private String avatar;
    private String title;
    private String content;
    private ArrayList<String> images;
    private String tags;
    private String[] tag;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
    private Integer likeNumber;
    private Integer visitNumber;
    private Integer commentNumber;
    private int state;


}
