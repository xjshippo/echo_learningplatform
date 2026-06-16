package com.jxau.service;

import java.util.HashMap;

import com.jxau.util.ResultEntity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "rc-index",contextId = "resource")
@Service
public interface ResourceService {
    @PostMapping(value = "/index/get/all/resource",consumes = "application/json")
    public ResultEntity<HashMap<String,Object>> getAllResource(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/get/all/video",consumes = "application/json")
    public ResultEntity<HashMap<String,Object>> getAllVideo(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/look/one/video",consumes = "application/json")
    public ResultEntity<String> lookOneVideo(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/delete/resource",consumes = "application/json")
    public ResultEntity<String> deleteResource(@RequestBody HashMap<String,Object> map);
}

