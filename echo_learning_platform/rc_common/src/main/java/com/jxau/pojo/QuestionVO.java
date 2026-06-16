package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 单选和多选
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionVO {

    private List<MultipleChoicePO> MultipleChoiceList;// 单选题
    /*private List<MultipleChoicesPO> MultipleChoicesList;// 多选题*/
    private List<String> answers;// 正确选项

}
