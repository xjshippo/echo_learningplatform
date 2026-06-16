package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesPO {
        // 收藏夹
        private String id;
        private String userId;
        private String name;
        private Date createTime;


}
