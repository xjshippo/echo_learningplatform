package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfessionalVO {

    private String userId;
    private String professional;
    private List<String> invitationId;// 点赞过的文章id集合
}
