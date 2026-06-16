package com.jxau.controller;

import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;
import com.jxau.pojo.Essay;
import com.jxau.pojo.EssayFileVO;
import com.jxau.pojo.EssayVO;
import com.jxau.service.CommentService;
import com.jxau.service.EssayService;
import com.jxau.service.ResourceService;
import com.jxau.util.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private EssayService essayService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private CommentService commentService;
    
    @PostMapping("/add/one/essay")
    @Authorize
    public ResultEntity<String> addOneEssay(@RequestBody HashMap<String,Object> map, HttpServletRequest request){
        String userId = (String) request.getAttribute("currentUser");
        map.put("userId", userId);
        try{
            return essayService.addOneEssay(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
    
    @PostMapping("/add/one/essay/image")
    public ResultEntity<String> addOneImage(@RequestParam("invitationId") String invitationId, @RequestPart("file") MultipartFile[] file, @RequestParam Boolean flag){
        try{
            return essayService.addOneImage(invitationId, file,flag);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/delete/essay")
    public ResultEntity<String> deleteEssay(@RequestBody HashMap<String,Object> map){
        try{
            return essayService.deleteEssay(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/all/essays")
    public ResultEntity<PageInfo<Essay>> selectAllEssay(@RequestBody HashMap<String,String> map){
        try{
            return essayService.selectAllEssay(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/all/essays/recommend")
    public ResultEntity<PageInfo<Essay>> selectAllEssayRecommend(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        String userId = (String) request.getAttribute("currentUser");
        map.put("userId", userId != null ? userId : "");
        try{
            return essayService.selectAllEssayRecommend(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/file/essays")
    public ResultEntity<List<EssayFileVO>> selectFileEssayRecommend(){
        try{
            return essayService.selectFileEssayRecommend();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/update/one/essay")
    @Authorize
    public ResultEntity<String> updateOneEssay(@RequestBody HashMap<String,String> map, HttpServletRequest request){
        String userId = (String) request.getAttribute("currentUser");
        map.put("userId", userId);
        try{
            return essayService.updateOneEssay(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/one/essays")
    public ResultEntity<EssayVO> selectOneEssay(@RequestBody HashMap<String,String> map){
        try{
            return essayService.selectOneEssay(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/like/one/essay")
    @Authorize
    public ResultEntity<String> likeOneEssay(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        String userId = (String) request.getAttribute("currentUser");
        map.put("userId",userId);
        try{
            return essayService.likeOneEssay(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    // ========== 资源接口 ==============

    @PostMapping("get/all/resource")
    public ResultEntity<HashMap<String,Object>> getAllResource(@RequestBody HashMap<String,String> map){
        try{
            return resourceService.getAllResource(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("get/all/video")
    public ResultEntity<HashMap<String,Object>> getAllVideo(@RequestBody HashMap<String,String> map){
        try{
            return resourceService.getAllVideo(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("look/one/video")
    @Authorize
    public ResultEntity<String> lookOneVideo(@RequestBody HashMap<String,String> map){
        try{
            return resourceService.lookOneVideo(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/delete/resource")
    public ResultEntity<String> deleteResource(@RequestBody HashMap<String,Object> map){
        try{
            return resourceService.deleteResource(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    // ============ 评论接口 =============

    @PostMapping("/add/one/comment")
    @Authorize
    public ResultEntity<String> addOneComment(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        String userId = (String) request.getAttribute("currentUser");
        map.put("userId",userId);
        try{
            return commentService.addOneComment(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/all/comment")
    public ResultEntity<HashMap<String,Object>> getAllComment(@RequestBody HashMap<String,String> map){
        try{
            return commentService.getAllComment(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/add/re/comment")
    @Authorize
    public ResultEntity<String> addReOneComment(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return commentService.addReOneComment(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/like/one/comment")
    @Authorize
    public ResultEntity<String> likeOneComment(@RequestBody HashMap<String,String> map,HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute("currentUser");
            map.put("userId",userId);
            return commentService.likeOneComment(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/get/all/re/comment")
    public ResultEntity<HashMap<String,Object>> getAllReComment(@RequestBody HashMap<String,String> map){
        try{
            return commentService.getAllReComment(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
}
