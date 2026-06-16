package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryPO {

    private String id;
    private String viewId;
    private Date createTime;
    private String userId;
}
