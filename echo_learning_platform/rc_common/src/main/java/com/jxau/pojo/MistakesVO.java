package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 错题
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MistakesVO {

    private String questionId;// 题目id
    private String type;// 题目类型
    private String userChoice;// 用户选项
    private String analysis;// 分析
    private String rightChoice;// 正确答案

}
