package com.jxau.service;

import com.jxau.pojo.UserMessage;
import com.jxau.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("rc-webMessage")
@Service
public interface MessageService {

    @GetMapping("/message/list")
    ResultEntity<List<UserMessage>> getMessageList(@RequestParam("type") String type,
                                                   @RequestParam("userId") String userId);
}
