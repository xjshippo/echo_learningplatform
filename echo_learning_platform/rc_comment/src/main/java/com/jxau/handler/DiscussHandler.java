package com.jxau.handler;

import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;
import com.jxau.pojo.*;
import com.jxau.service.DiscussService;
import com.jxau.util.ResultEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;


@Slf4j
@Api(tags = "讨论区")
@RestController
@RequestMapping("/discuss")
public class DiscussHandler {

    @Autowired
    DiscussService discussService;

    @ApiOperation(value = "讨论管理-添加讨论区",notes = "添加讨论区")
    @PostMapping("/add/one/discuss")
    public ResultEntity<String> addOneDiscuss(@RequestBody HashMap<String,String> map, MultipartFile file){

        String endMessage = discussService.addOneDiscuss(map,file);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "讨论管理-用户关注一个讨论区",notes = "关注讨论区")
    @PostMapping("/add/one/like/discuss")
    public ResultEntity<String> addOneLikeDiscuss(@RequestBody HashMap<String,String> map){
        String endMessage = discussService.addOneLikeDiscuss(map);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "讨论管理-查看用户关注的讨论区",notes = "查看用户关注的讨论区")
    @PostMapping("/get/user/discuss")
    @Authorize
    public ResultEntity<HashMap> getUserLikeDiscuss(@RequestBody HashMap<String,String> map){
        HashMap result = new HashMap();
        PageInfo<DiscussPO> pageInfo  = discussService.getUserLikeDiscuss(map);
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查看失败");
        }else{
            result.put("discuss",pageInfo.getList());
            result.put("pages",pageInfo.getPages());
            result.put("pageSize",pageInfo.getPageSize());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("total", pageInfo.getTotal());
            return ResultEntity.successWithData(result);

        }
    }

    @ApiOperation(value = "讨论管理-查看所有的讨论区",notes = "查看所有的讨论区")
    @PostMapping("/get/all/discuss")
    public ResultEntity<HashMap> getAllDiscuss(@RequestBody HashMap<String,String> map){
        HashMap result = new HashMap();
        PageInfo<DiscussPO> pageInfo = discussService.getAllDiscuss(map);
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查看失败");
        }else{
            result.put("discuss",pageInfo.getList());
            result.put("pages",pageInfo.getPages());
            result.put("pageSize",pageInfo.getPageSize());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("total", pageInfo.getTotal());

            return ResultEntity.successWithData(result);
        }
    }

    @ApiOperation(value = "讨论管理-用户取消关注",notes = "取消关注讨论区")
    @PostMapping("/unlike/one/discuss")

    public ResultEntity<String> unOneLikeDiscuss(@RequestBody HashMap<String,String> map){
        String endMessage = discussService.unOneLikeDiscuss(map);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "讨论管理-用户发表帖子")
    @PostMapping("/add/one/discuss/comment")
    public ResultEntity<String> addOneDiscussComment(@RequestBody HashMap<String,String> map){
        String endMessage = discussService.addOneDiscussComment(map);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "文章管理-添加图片",notes = "用户添加一篇文章")
    @PostMapping("/add/one/discuss/image")
    public ResultEntity<String> addOneImage(@RequestParam String commentId, MultipartFile[] file){
        String endMessage = discussService.addOneImage(commentId, file[0]);
        if (!ResultEntity.SUCCESS.equals(endMessage)){
            return ResultEntity.falseWithoutData(endMessage);
        }
        return ResultEntity.successWithoutData();
    }

    @ApiOperation(value = "讨论管理-用户点赞/浏览帖子",notes = "用户点赞")
    @PostMapping("/user/like/one/discuss/comment")
    public ResultEntity<String> likeOneDiscussComment(@RequestBody HashMap<String,String> map){
        String endMessage = discussService.likeOneDiscussComment(map);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "讨论管理-用户回复帖子",notes = "用户回复")
    @PostMapping("/user/re/one/discuss/comment")
    public ResultEntity<String> addReOneDiscuss(@RequestBody HashMap<String,String> map){
        String endMessage = discussService.addReOneDiscussComment(map);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "讨论管理-用户查看所有帖子",notes = "用户查看帖子")
    @PostMapping("/user/get/all/discuss/comment")
    public ResultEntity<HashMap<String,Object>> getAllDiscussComment(@RequestBody HashMap<String,String> map){
        HashMap<String,Object> result = new HashMap<>();
        PageInfo<DiscussCommentPO> pageInfo = discussService.getAllDiscussComment(map);
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查看失败");
        }else{
            ArrayList<DiscussCommentVO> commentVOS = new ArrayList<>();
            for (DiscussCommentPO tmp: pageInfo.getList()) {
                ArrayList<String> images = new ArrayList<>();
                for (ImagePo d: tmp.getImagePos()) {
                    images.add(d.getUrl());
                }
                commentVOS.add(new DiscussCommentVO(
                    tmp.getId(),
                    tmp.getUser().getNickName(),
                        tmp.getUser().getImageUrl(),
                        tmp.getTitle(),
                        tmp.getContent(),
                        images,
                        tmp.getLikeNumber(),
                        tmp.getCollectNum(),
                        tmp.getCreate_time()
                ));
            }

            result.put("comment",commentVOS);
            result.put("pages",pageInfo.getPages());
            result.put("pageSize",pageInfo.getPageSize());
            result.put("total",pageInfo.getTotal());
            result.put("pageNum",pageInfo.getPageNum());
            return ResultEntity.successWithData(result);
        }
    }

    @ApiOperation(value = "讨论管理-用户查看所有回复",notes = "用户查看回复")
    @PostMapping("/user/get/all/discuss/re/comment")
    public ResultEntity<HashMap<String,Object>> getReDiscussComment(@RequestBody HashMap<String,String> map){
        HashMap<String,Object> result = new HashMap<>();
        PageInfo<DiscussCommentPO> pageInfo = discussService.getAllDiscussComment(map);
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查看失败");
        }else{
            ArrayList<DiscussComment_VO> commentVOS = new ArrayList<>();
            for (DiscussCommentPO tmp: pageInfo.getList()) {
                commentVOS.add(new DiscussComment_VO(
                        tmp.getUser().getNickName(),
                        tmp.getUser().getImageUrl(),
                        tmp.getCreate_time(),
                        tmp.getContent()
                ));
            }
            result.put("reComment",commentVOS);
            result.put("pages",pageInfo.getPages());
            result.put("pageSize",pageInfo.getPageSize());
            result.put("total",pageInfo.getTotal());
            result.put("pageNum",pageInfo.getPageNum());
            return ResultEntity.successWithData(result);
        }
    }

    @ApiOperation(value = "用户查看所有的分类",notes = "查看分类")
    @PostMapping("/get/all/type")
    public ResultEntity<HashMap> getAllType(@RequestBody HashMap<String,String> map){
        HashMap result = new HashMap();
        PageInfo<TypePO> pageInfo = discussService.getAllType(map);
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查看失败");
        }else{
            result.put("types",pageInfo.getList());
            result.put("pages",pageInfo.getPages());
            result.put("pageSize",pageInfo.getPageSize());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("total", pageInfo.getTotal());
            return ResultEntity.successWithData(result);
        }
    }

    @ApiOperation(value = "讨论管理-查看一个讨论",notes = "查看一个帖子")
    @PostMapping("/get/one/discuss/comment")
    public ResultEntity<HashMap<String,Object>> selectOneDiscussComment(@RequestBody HashMap<String,String> map){
        DiscussCommentPO tmp = discussService.selectOneDiscussComment(map);
        HashMap<String,Object> result = new HashMap<>();
        if (tmp == null){
            return ResultEntity.falseWithoutData("查询失败");
        }else{
            map.put("visitNumber",tmp.getVisitNumber()+1+"");
            String s = discussService.likeOneDiscussComment(map);
            if (!s.equals(ResultEntity.SUCCESS)){
                return ResultEntity.falseWithoutData("观看数添加失败");
            }
        }
        ArrayList<String> images = new ArrayList<>();
        for (ImagePo d: tmp.getImagePos()) {
            images.add(d.getUrl());
        }
        DiscussCommentVO discussCommentVO = new DiscussCommentVO(
                tmp.getId(),
                tmp.getUser().getNickName(),
                tmp.getUser().getImageUrl(),
                tmp.getTitle(),
                tmp.getContent(),
                images,
                tmp.getLikeNumber(),
                tmp.getCollectNum(),
                tmp.getCreate_time()
        );
        // String [] commentId = discussService.selectAllLikeComment(map);
        // result.put("hasLikedArr",commentId);
        result.put("comment",discussCommentVO);
        return ResultEntity.successWithData(result);
    }
}
