package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 讨论中间表
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDiscussCommentPO {
    private String id;
    private String dcId;
    private String uId;
}
