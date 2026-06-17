package com.jxau.controller;

import com.jxau.mapper.MessageMapper;
import com.jxau.pojo.UserMessage;
import com.jxau.util.ResultEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class MessageController {

    @Resource
    private MessageMapper messageMapper;

    @GetMapping("/message/list")
    public ResultEntity<List<UserMessage>> getMessageList(@RequestParam("type") String type,
                                                          @RequestParam("userId") String userId) {
        List<UserMessage> list = messageMapper.selectMessagesByType(userId, type);
        return ResultEntity.successWithData(list);
    }
}
