package com.jxau.service;

import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jxau.config.FeignSupportConfig;
import com.jxau.pojo.Essay;
import com.jxau.pojo.EssayFileVO;
import com.jxau.pojo.EssayVO;
import com.jxau.util.ResultEntity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "rc-index",contextId = "essay",configuration= FeignSupportConfig.class)
@Service
public interface EssayService {

    @PostMapping(value = "/index/add/one/essay",consumes = "application/json")
    ResultEntity<String> addOneEssay(@RequestBody HashMap<String, Object> map);

    @PostMapping(value = "/index/add/one/essay/image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultEntity<String> addOneImage(@RequestParam(value = "invitationId") String invitationId, @RequestPart (value = "file")MultipartFile[] file, @RequestParam(value = "flag") Boolean flag);
    
    @PostMapping(value = "/index/delete/essay",consumes = "application/json")
    public ResultEntity<String> deleteEssay(@RequestBody HashMap<String,Object> map);

    @PostMapping(value = "/index/get/all/essays",consumes = "application/json")
    public ResultEntity<PageInfo<Essay>> selectAllEssay(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/get/all/essays/recommend",consumes = "application/json")
    public ResultEntity<PageInfo<Essay>> selectAllEssayRecommend(@RequestBody HashMap<String,String> map);

    @PostMapping("/index/get/file/essays")
    public ResultEntity<List<EssayFileVO>> selectFileEssayRecommend();

    @PostMapping(value = "/index/update/one/essay",consumes = "application/json")
    public ResultEntity<String> updateOneEssay(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/get/one/essays",consumes = "application/json")
    public ResultEntity<EssayVO> selectOneEssay(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/like/one/essay",consumes = "application/json")
    public ResultEntity<String> likeOneEssay(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/index/get/all/re/comment",consumes = "application/json")
    public ResultEntity<HashMap<String,Object>> getAllReComment(@RequestBody HashMap<String,String> map);
}
