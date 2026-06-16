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
@NoArgsConstructor
@AllArgsConstructor
public class EssayPOVO {

    private String id;
    private String title;
    private String content;
    private String userName;// 作者的姓名
    private Integer likeNumber;
    private Integer visitNumber;
    private Integer commentNumber;
    private String contentImg;



}
