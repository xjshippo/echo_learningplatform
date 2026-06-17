package com.jxau.controller;

import com.github.pagehelper.PageInfo;
import com.jxau.pojo.*;
import com.jxau.service.DiscussService;
import com.jxau.service.MessageService;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
public class MessageAndCommunityController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussService discussService;

    // ========== 消息中心 ==========

    @GetMapping("/message/list")
    public ResultEntity<List<UserMessage>> getMessageList(@RequestParam("type") String type,
                                                          HttpServletRequest request) {
        String userId = (String) request.getAttribute("currentUser");
        if (userId == null) {
            userId = request.getHeader("currentUser");
        }
        return messageService.getMessageList(type, userId != null ? userId : "");
    }

    // ========== 社区 ==========

    @PostMapping("/community/join")
    public ResultEntity<String> joinCommunity(@RequestBody HashMap<String, String> map) {
        // isJoined=true 加入, false 退出
        String isJoined = map.get("isJoined");
        if ("true".equals(isJoined)) {
            return discussService.addOneLikeDiscuss(map);
        } else {
            return discussService.unOneLikeDiscuss(map);
        }
    }

    @GetMapping("/community/status")
    public ResultEntity<HashMap> getCommunityStatus(HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<>();
        String userId = (String) request.getAttribute("currentUser");
        if (userId == null) {
            userId = request.getHeader("currentUser");
        }
        map.put("userId", userId);
        map.put("currentPage", "1");
        map.put("pageSize", "50");
        return discussService.getUserLikeDiscuss(map);
    }

    @PostMapping("/community/posts")
    public ResultEntity<HashMap<String, Object>> getCommunityPosts(@RequestBody HashMap<String, String> map) {
        return discussService.getAllDiscussComment(map);
    }
}
