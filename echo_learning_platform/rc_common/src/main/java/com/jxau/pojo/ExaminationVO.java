package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationVO {

    private Double passRate;// 正确率
    private List<String> faultQuestions;//错误的题号列表
    private List<String> answerList;// 正确答案列表
    private Integer correctNumber;// 正确的数量
    private Integer WrongNumber;// 错误的数量
    private Integer totalNumber;// 总题数
    private ProblemSetsVO question;// 题组信息

}
