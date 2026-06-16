package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

        private String id;
        private String userId;
        private String sex;
        private String address;
        private String phone;
        private String QQ;
        private String email;
        private String skills_tree_id;
        private String medals;
        private String messageId;
        private String professional;
        private String imgUrl;// 用户头像
        private List<String> professionals;

}
