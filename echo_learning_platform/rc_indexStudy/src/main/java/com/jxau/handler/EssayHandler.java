package com.jxau.handler;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;

import com.jxau.pojo.*;
import com.jxau.service.CommentService;
import com.jxau.service.EssayService;
import com.jxau.util.PageInfoUtils;
import com.jxau.util.ResultEntity;
import com.netflix.client.http.HttpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  文章接口
 * @author cheng
 */
@Slf4j
@Api(tags = "文章")
@RestController
@RequestMapping("/index")
public class EssayHandler {
    @Autowired
    private EssayService essayService;
    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "文章管理-添加文章",notes = "用户添加一篇文章")
    @PostMapping("/add/one/essay")
    public ResultEntity<String> addOneEssay(@RequestBody HashMap<String,Object> map){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = (String) request.getAttribute("currentUser");
        if (userId == null) {
            userId = request.getHeader("currentUser");
        }
        if (userId != null) {
            map.put("userId", userId);
        }
        ResultEntity resultEntity = essayService.addOneEssay(map);
        String endMessage = resultEntity.getResult();
        if (!ResultEntity.SUCCESS.equals(endMessage)){
            return ResultEntity.falseWithoutData(endMessage);
        }
        return resultEntity;
    }
    
    @ApiOperation(value = "文章管理-添加图片",notes = "用户添加一篇文章")
    @PostMapping("/add/one/essay/image")
    public ResultEntity<String> addOneImage(@RequestParam String invitationId,@RequestPart(value = "file") MultipartFile[] file,@RequestParam Boolean flag){
        String endMessage = essayService.addOneImage(invitationId, file[0],flag);
        if (!ResultEntity.SUCCESS.equals(endMessage)){
            return ResultEntity.falseWithoutData(endMessage);
        }
        return ResultEntity.successWithoutData();
    }

    @ApiOperation(value = "文章管理-删除文章",notes = "用户删除一篇或者多篇文章")
    @PostMapping("/delete/essay")
    public ResultEntity<String> deleteEssay(@RequestBody HashMap<String,Object> map){
        String endMessage = essayService.deleteEssay(map);
        if (!ResultEntity.SUCCESS.equals(endMessage)){
            return ResultEntity.falseWithoutData(endMessage);
        }
        return ResultEntity.successWithoutData();
    }

    @ApiOperation(value = "文章管理-查看所有文章（未加大数据算法版）",notes = "用户查看搜索的文章")
    @PostMapping("/get/all/essays")
    public ResultEntity<PageInfo<Essay>> selectAllEssay(@RequestBody HashMap<String,String> map){
        PageInfo<Essay> pageInfo = essayService.selectAllEssays(map);
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查询失败");
        }
        return ResultEntity.successWithData(pageInfo);
    }

    @ApiOperation(value = "文章管理-查看所有文章(推荐算法)",notes = "用户查看所有文章")
    @PostMapping("/get/all/essays/recommend")
    public ResultEntity<PageInfo<Essay>> selectAllEssayRecommend(@RequestBody HashMap<String,String> map){

        if ("true".equals(map.get("flag"))){
            PageInfo<Essay> pageInfo = essayService.selectAllEssays(map);
            if (pageInfo == null){
                return ResultEntity.falseWithoutData("查询失败");
            }
            return ResultEntity.successWithData(pageInfo);
        }else{
            int currentPage = Integer.parseInt(map.get("currentPage"));
            // 页面条数
            int pageSize = Integer.parseInt(map.get("pageSize"));
            List<Essay> essays = essayService.selectAllEssaysRecommend(map);
            if (essays == null){
                return ResultEntity.falseWithoutData("查询失败");
            }
            PageInfo<Essay> pageInfo = PageInfoUtils.list2PageInfo(essays, currentPage, pageSize);

            return ResultEntity.successWithData(pageInfo);
        }

    }

    @ApiOperation(value = "文章管理-查看最热十篇文章",notes = "用户查看最热文章")
    @PostMapping("/get/file/essays")
    public ResultEntity<List<EssayFileVO>> selectFileEssayRecommend(){
        List<EssayFileVO> fileVOS = essayService.selectFileEssays();
        if (fileVOS == null){
            return ResultEntity.falseWithoutData("查询失败");
        }

        return ResultEntity.successWithData(fileVOS);

    }

    @ApiOperation(value = "文章管理-修改文章",notes = "用户修改文章")
    @PostMapping("/update/one/essay")
    public ResultEntity<String> updateOneEssay(@RequestBody HashMap<String,String> map){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = (String) request.getAttribute("currentUser");
        if (userId == null) {
            userId = request.getHeader("currentUser");
        }
        if (userId != null) {
            map.put("userId", userId);
        }
        String endMessage = essayService.updateEssay(map);
        if (!ResultEntity.SUCCESS.equals(endMessage)){
            return ResultEntity.falseWithoutData(endMessage);
        }
        return ResultEntity.successWithoutData();
    }

    @ApiOperation(value = "文章管理-查看一篇文章",notes = "用户查看一篇文章")
    @PostMapping("/get/one/essays")
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<EssayVO> selectOneEssay(@RequestBody HashMap<String,String> map){
        EssayVO e = null;
        Essay essay = essayService.selectOneEssays(map);
        if (essay == null){
            return ResultEntity.falseWithoutData("查询失败");
        }else{
            map.put("visitNumber",essay.getVisitNumber()+1+"");
            essay.setVisitNumber(essay.getVisitNumber()+1);
            ArrayList<String> images = new ArrayList<>();
            if (essay.getImages() != null) {
                for (ImagePo imagePo: essay.getImages()) {
                    images.add(imagePo.getUrl());
                }
            }
            e = new EssayVO(
                essay.getId(),
                essay.getUser() != null ? essay.getUser().getNickName() : "",
                essay.getUser() != null ? essay.getUser().getImageUrl() : "",
                essay.getTitle(),
                essay.getContent(),
                images,
                essay.getTag(),
                essay.getCreate_time(),
                essay.getLikeNumber(),
                essay.getVisitNumber(),
                essay.getCommentNumber()
            );
            LikeEssayVO likeEssayVO = essayService.selectOneLikeEassy(map);
            if (likeEssayVO == null){
                e.setThumbStatus(false);
            }else{
                e.setThumbStatus(true);
            }
            // 加入浏览历史
            if (map.get("userId") != null && !map.get("userId").isEmpty()) {
                essayService.insertHisttoryView(map.get("userId"),map.get("invitationId"));
            }

            String s = essayService.updateEssayNumber(map);
            if (!s.equals(ResultEntity.SUCCESS)){
                return ResultEntity.falseWithoutData("观看数添加失败");
            }

        }
        return ResultEntity.successWithData(e);
    }

    @ApiOperation(value = "文章管理-文章点赞",notes = "用户给文章点赞")
    @PostMapping("/like/one/essay")
    public ResultEntity<String> likeOneEssay(@RequestBody HashMap<String,String> map){

        String endMessage = essayService.updateEssayNumber(map);
        if (!ResultEntity.SUCCESS.equals(endMessage)){
            return ResultEntity.falseWithoutData(endMessage);
        }
        return ResultEntity.successWithoutData();
    }
}
