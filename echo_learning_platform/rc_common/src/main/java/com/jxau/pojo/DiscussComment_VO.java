package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussComment_VO {
    private String userName;
    private String userImg;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date sendTime;
    private String commentContent;
}
