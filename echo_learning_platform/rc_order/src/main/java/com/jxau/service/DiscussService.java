package com.jxau.service;



import com.jxau.config.FeignSupportConfig;
import com.jxau.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;

/**
 * @author cheng
 */
@FeignClient(name = "rc-comment",configuration= FeignSupportConfig.class)
@Service
public interface DiscussService {

/*
    @PostMapping(value = "/discuss/add/one/discuss",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultEntity<String> addOneDiscuss(@RequestBody HashMap<String,String> map, @RequestPart("file") MultipartFile file);
*/

    @PostMapping(value = "/discuss/add/one/like/discuss",consumes = "application/json")
    public ResultEntity<String> addOneLikeDiscuss(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/discuss/get/user/discuss",consumes = "application/json")
    public ResultEntity<HashMap> getUserLikeDiscuss(@RequestBody HashMap<String,String> map);
    
    @PostMapping(value = "/discuss/get/all/discuss",consumes = "application/json")
    public ResultEntity<HashMap> getAllDiscuss(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/discuss/unlike/one/discuss",consumes = "application/json")
    public ResultEntity<String> unOneLikeDiscuss(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/dsicuss/add/one/discuss/comment",consumes = "application/json")
    public ResultEntity<String> addOneDiscussComment(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/discuss/add/one/discuss/image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultEntity<String> addOneImage(@RequestParam("commentId") String commentId,  @RequestPart (value = "file")MultipartFile[] file);

    @PostMapping(value = "/discuss/user/like/one/discuss/comment",consumes = "application/json")
    public ResultEntity<String> likeOneDiscussComment(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/discuss/user/re/one/discuss/comment",consumes = "application/json")
    public ResultEntity<String> addReOneDiscuss(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/discuss/user/get/all/discuss/comment",consumes = "application/json")
    public ResultEntity<HashMap<String,Object>> getAllDiscussComment(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/discuss/user/get/all/discuss/re/comment",consumes = "application/json")
    public ResultEntity<HashMap<String,Object>> getReDiscussComment(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/discuss/get/all/type",consumes = "application/json")
    public ResultEntity<HashMap> getAllType(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/discuss/get/one/discuss/comment",consumes = "application/json")
    public ResultEntity<HashMap<String,Object>> selectOneDiscussComment(@RequestBody HashMap<String,String> map);
}
