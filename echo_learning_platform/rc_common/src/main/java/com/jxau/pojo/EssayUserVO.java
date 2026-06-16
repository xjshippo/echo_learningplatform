package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EssayUserVO {

    private String id;
    private String title;
    private String content;
    private UserEssayVO user;
    private String userId;
    private String tags;
    private String[] tag;
    /**
     * 文章 0 待审核；1 删除标记；3正常显示 ；4被举报带审核；
     */
    private Integer state;
    private Integer likeNumber;
    private Integer visitNumber;
    private Integer commentNumber;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date create_time;
    private String coverImgUrl;
    private ArrayList<ImagePo> images;

}
