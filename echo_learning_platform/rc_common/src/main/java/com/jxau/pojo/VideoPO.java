package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPO {
    private String id;
    private String content;
    private String url;
    private String imageUrl;
    private String userId;
    private UserPO user;
    private String category;
    private ArrayList<LookUserPo> lookUserPos;
    private Date create_time;
    private Integer state;
}
