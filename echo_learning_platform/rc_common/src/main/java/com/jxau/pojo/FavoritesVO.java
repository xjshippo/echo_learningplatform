package com.jxau.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritesVO {

    private String favoritesId;
    private String userId;
    private String invitationId;
    private Date createTime;

}
