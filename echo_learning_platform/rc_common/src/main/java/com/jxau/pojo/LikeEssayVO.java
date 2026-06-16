package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点赞中间表
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeEssayVO {
    private String id;
    private String iId;
    private String uId;
}
