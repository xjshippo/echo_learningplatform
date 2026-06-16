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
public class MultipleChoiceVO extends  MultipleChoicePO{

    private String error_choice;

}
