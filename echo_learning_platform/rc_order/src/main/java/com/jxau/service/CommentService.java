package com.jxau.service;

import java.util.HashMap;

import com.jxau.util.ResultEntity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author chenzouquan
 */
@FeignClient(name = "rc-index",contextId = "comment")
@Service
public interface CommentService {

    @PostMapping(value = "/index/add/one/comment",consumes = "application/json")
    public ResultEntity<String> addOneComment(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/get/all/comment",consumes = "application/json")
    public ResultEntity<HashMap<String,Object>> getAllComment(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/add/re/comment",consumes = "application/json")
    public ResultEntity<String> addReOneComment(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/like/one/comment",consumes = "application/json")
    public ResultEntity<String> likeOneComment(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/get/all/re/comment",consumes = "application/json")
    public ResultEntity<HashMap<String,Object>> getAllReComment(@RequestBody HashMap<String,String> map);

}
