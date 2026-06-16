package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private String id;
    private String userId;
    private String sex;
    private String address;
    private String phone;
    private String QQ;
    private String email;
    private String skillsTreeId;
    private String medals;
    private String messageId;
    private String professional;

}
