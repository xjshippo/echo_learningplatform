package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 单选题
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoicePO {

    private String uuid;
    private String content;
    private List<ChoicesVO> choices;
    private String source;
    private String analysis;
    private String tags;
    private String question_type;
    private String op_one;
    private String op_two;
    private String op_three;
    private String op_four;
    private String op_answer;



}
