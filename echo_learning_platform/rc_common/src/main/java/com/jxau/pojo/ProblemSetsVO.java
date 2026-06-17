package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 题组
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSetsVO {

    private String id;
    private List<TagVO> tagList;
    private String title;
    private Double passRate;
    private Integer number;
    private Integer questionCount;
    private Integer passNumbers;
    private Date create_time;
    private String level;
    private Integer isPaid;


}
