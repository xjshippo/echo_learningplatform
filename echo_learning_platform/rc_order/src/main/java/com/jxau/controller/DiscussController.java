package com.jxau.controller;

import com.jxau.annotations.Authorize;
import com.jxau.service.DiscussService;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author cheng
 */
@RestController
@RequestMapping("/discuss")
public class DiscussController {
    @Autowired
    private DiscussService discussService;

/*
    @PostMapping("/add/one/discuss")
    @Authorize
    public ResultEntity<String> addOneDiscuss(@RequestBody HashMap<String,String> map,@RequestPart("file")  MultipartFile file, HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return discussService.addOneDiscuss(map,file);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
*/

    @PostMapping("/add/one/like/discuss")
    @Authorize
    public ResultEntity<String> addOneLikeDiscuss(@RequestBody HashMap<String,String> map, HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return discussService.addOneLikeDiscuss(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/user/discuss")
    @Authorize
    public ResultEntity<HashMap> getUserLikeDiscuss(@RequestBody HashMap<String,String> map, HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return discussService.getUserLikeDiscuss(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/all/discuss")
    public ResultEntity<HashMap> getAllDiscuss(@RequestBody HashMap<String,String> map){
        try{
            return discussService.getAllDiscuss(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/unlike/one/discuss")
    @Authorize
    public ResultEntity<String> unOneLikeDiscuss(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return discussService.unOneLikeDiscuss(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/add/one/discuss/comment")
    @Authorize
    public ResultEntity<String> addOneDiscussComment(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return discussService.addOneDiscussComment(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/add/one/discuss/image")
    public ResultEntity<String> addOneImage(@RequestParam String commentId, @RequestPart("file") MultipartFile[] file){
        try{
            return discussService.addOneImage(commentId,file);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/user/like/one/discuss/comment")
    @Authorize
    public ResultEntity<String> likeOneDiscussComment(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return discussService.likeOneDiscussComment(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/user/re/one/discuss/comment")
    @Authorize
    public ResultEntity<String> addReOneDiscuss(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return discussService.addReOneDiscuss(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/user/get/all/discuss/comment")
    public ResultEntity<HashMap<String,Object>> getAllDiscussComment(@RequestBody HashMap<String,String> map){
        try{
            return discussService.getAllDiscussComment(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/user/get/all/discuss/re/comment")
    public ResultEntity<HashMap<String,Object>> getReDiscussComment(@RequestBody HashMap<String,String> map){
        try{
            return discussService.getReDiscussComment(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/all/type")
    public ResultEntity<HashMap> getAllType(@RequestBody HashMap<String,String> map){
        try{
            return discussService.getAllType(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/one/discuss/comment")
    public ResultEntity<HashMap<String,Object>> selectOneDiscussComment(@RequestBody HashMap<String,String> map){
        try{
            return discussService.selectOneDiscussComment(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
}
