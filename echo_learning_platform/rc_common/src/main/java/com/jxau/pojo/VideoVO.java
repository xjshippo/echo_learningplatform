package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author chenzouquan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoVO {
    private String uuid;
    private String avatar;
    private String userName;
    private String title;
    private String category;
    private String[] avatarList;
    private String poster;
    private String videoUrl;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
}
