package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论 帖子实体类
 * @author chenzouquan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
    private String id;
    private String iId;
    private String cId;
    private CommentPO commentPO;
}
