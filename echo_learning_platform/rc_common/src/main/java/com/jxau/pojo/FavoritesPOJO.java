package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesPOJO {
        // 收藏夹
        private String id;
        private Integer num;// 收藏夹中的文章数量
        private String name;
        private Date createTime;


}
