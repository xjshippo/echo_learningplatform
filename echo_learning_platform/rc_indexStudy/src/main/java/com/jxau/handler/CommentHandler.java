package com.jxau.handler;

import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;
import com.jxau.pojo.CommentPO;
import com.jxau.pojo.Essay;
import com.jxau.pojo.EssayCommentVO;
import com.jxau.service.CommentService;
import com.jxau.service.EssayService;
import com.jxau.util.ResultEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  评论接口
 * @author cheng
 */
@Slf4j
@Api(tags = "评论")
@RestController
@RequestMapping("/index")
public class CommentHandler {

    @Autowired
    CommentService commentService;
    @Autowired
    EssayService essayService;
    @ApiOperation(value = "评论管理-添加评论",notes = "用户评论")
    @PostMapping("/add/one/comment")
    public ResultEntity<String> addOneComment(@RequestBody HashMap<String,String> map){

        String endMessage = commentService.addOneComment(map);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "评论管理-查看评论",notes = "查看帖子评论")
    @PostMapping("/get/all/comment")
    public ResultEntity<HashMap<String,Object>> getAllComment(@RequestBody HashMap<String,String> map){
        HashMap<String,Object> map1 = new HashMap<>();
        PageInfo<CommentPO> pageInfo = commentService.getAllComment(map);
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查询失败");
        }
        ArrayList<EssayCommentVO> commentVOS = new ArrayList<>();
        for (CommentPO tmp: pageInfo.getList()) {
            commentVOS.add(new EssayCommentVO(
                    tmp.getId(),tmp.getParentId(),tmp.getInvitationId(),tmp.getContent(),tmp.getLikeNumber(),tmp.getVisitNumber()
                    ,tmp.getCommentNumber(),tmp.getCreate_time(),tmp.getState(),tmp.getUserPO().getNickName(),tmp.getUserPO().getImageUrl()
            ));
        }
        String [] commentId = essayService.selectAllLikeComment(map);
        map1.put("comment",commentVOS);
        map1.put("total",pageInfo.getTotal());
        map1.put("pages",pageInfo.getPages());
        map1.put("pageNum",pageInfo.getPageNum());
        map1.put("pageSize",pageInfo.getPageSize());
        map1.put("hasLikedArr",commentId);
        return ResultEntity.successWithData(map1);
    }

    @ApiOperation(value = "评论管理-回复评论",notes = "回复帖子评论")
    @PostMapping("/add/re/comment")
    public ResultEntity<String> addReOneComment(@RequestBody HashMap<String,String> map){
        String endMessage = commentService.addReOneComment(map);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "评论管理-点赞评论",notes = "帖子评论点赞")
    @PostMapping("/like/one/comment")
    public ResultEntity<String> likeOneComment(@RequestBody HashMap<String,String> map){
        String endMessage = commentService.likeOneComment(map);
        if (endMessage.equals(ResultEntity.SUCCESS)){
            return ResultEntity.successWithoutData();
        }else{
            return ResultEntity.falseWithoutData(endMessage);
        }
    }

    @ApiOperation(value = "评论管理-查看回复",notes = "查看帖子回复")
    @PostMapping("/get/all/re/comment")
    public ResultEntity<HashMap<String,Object>> getAllReComment(@RequestBody HashMap<String,String> map){
        HashMap<String,Object> map1 = new HashMap<>();
        ArrayList<CommentPO> pageInfo = commentService.getAllReComment(map);
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("回复查询失败");
        }
        ArrayList<EssayCommentVO> commentVOS = new ArrayList<>();
        for (CommentPO tmp: pageInfo) {
            commentVOS.add(new EssayCommentVO(
                    tmp.getId(),tmp.getParentId(),tmp.getInvitationId(),tmp.getContent(),tmp.getLikeNumber(),tmp.getVisitNumber()
                    ,tmp.getCommentNumber(),tmp.getCreate_time(),tmp.getState(),tmp.getUserPO().getNickName(),tmp.getUserPO().getImageUrl()
            ));
        }
        String [] commentId = essayService.selectAllLikeComment(map);
        map1.put("comment",commentVOS);
        map1.put("hasLikedArr",commentId);
        return ResultEntity.successWithData(map1);
    }
}
