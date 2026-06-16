package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemsetsUserPO {

        private String ProblemSetId;
        private String id;
        private String userId;
        private Date createTime;
        private Double passRate;

}
