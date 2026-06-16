package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MistakePO {

        private String id;
        private String questionId;
        private Date create_time;
        private String errorChoice;
        private String type;
        private String analysis;
        private String rightChoice;

}
