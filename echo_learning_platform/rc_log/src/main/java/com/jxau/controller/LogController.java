package com.jxau.controller;

import com.jxau.pojo.LogPO;
import com.jxau.service.LogService;
import com.jxau.util.JsonResult;
import com.jxau.util.ResultEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LogController {

    @Resource
    private LogService logService;

    @PostMapping("/rc/learnTree/log/save")
    public ResultEntity<String> logSave(@RequestBody LogPO logPO)
    {
        logService.insert(logPO);
        return ResultEntity.successWithoutData();
    }
}
