package com.jxau.service;

import com.jxau.pojo.LogPO;
import com.jxau.util.ResultEntity;

public interface LogService {

    ResultEntity<String> insert(LogPO logPO);

}
