package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 帖子图片实体类
 * @author chenzouquan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagePo {
    private String id;
    private String iid;
    private String url;
}
