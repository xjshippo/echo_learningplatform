package com.jxau.service;

import com.jxau.pojo.LogPO;
import com.jxau.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("rc-log-service")
public interface LogService {

    // 调用远程日志服务
    @PostMapping("/rc/learnTree/log/save")
    public ResultEntity<String> logSave(@RequestBody LogPO logPO);
}
