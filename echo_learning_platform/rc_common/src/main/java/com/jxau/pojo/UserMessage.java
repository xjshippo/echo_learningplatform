package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 信息实体类
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    /**
     * 消息发送者
     */
    private String userId;

    /**
     * 聊天文本
     */
    private String content;

    /**
     * 消息接受者
     */
    private String getUserId;

    /**
     * 发送时间
     */
    private Date createTime;
}
