package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 讨论区实体类
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussPO {
    private String id;
    private String discuss_name;
    private String tags;
    private Date create_time;
    private String image_url;
    private Integer number;
}
