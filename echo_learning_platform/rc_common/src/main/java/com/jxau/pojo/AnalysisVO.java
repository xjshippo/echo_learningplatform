package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 题目解析
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisVO {

    private String id;
    private String question_type;
    private String content;
    private Boolean isCorrect;
    private List user_choice;
    private String right_choice;
    private List<ChoicesVO> choices;
    private String analysis_content;
    private Boolean withinWrongBook;// 是否在错题本上
    private Boolean hasAnalysis;// 是否有解析

}
