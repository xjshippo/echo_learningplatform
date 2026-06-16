package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogPO {

        private String id;
        private String ip;
        private Date createTime;
        private String userId;
        private String content;
        private String userName;
        private String type;
        private String code;

}
