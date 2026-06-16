package com.jxau.pojo;

import com.jxau.annotations.Authorize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论点赞中间表
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeCommentVO {
    private String id;
    private String cId;
    private String uId;
}
