package com.jxau.service.impl;

import com.jxau.mapper.LogMapper;
import com.jxau.pojo.LogPO;
import com.jxau.service.LogService;
import com.jxau.util.JsonResult;
import com.jxau.util.ResultEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public ResultEntity<String> insert(LogPO logPO) {
        try {
            logMapper.insert(logPO);
            return ResultEntity.successWithoutData();
        }catch (Exception e)
        {
            e.printStackTrace();
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }
}
