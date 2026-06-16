package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源点赞中间表
 * @author cheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResourceVO {
    private String id;
    private String rId;
    private String uId;
}
