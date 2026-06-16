package com.jxau.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxau.mapper.CommentMapper;
import com.jxau.mapper.EssayMapper;
import com.jxau.pojo.*;
import com.jxau.service.CommentService;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 评论实现类
 * @author chenzouquan
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    EssayMapper essayMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addOneComment(HashMap<String, String> map) {
        String cid = UUID.randomUUID().toString().replace("-","");
        CommentPO commentPO = new CommentPO(
                cid,
                "",
                map.get("invitationId"),
                map.get("content"),
                0,
                0,
                0,
                new Date(),
                1,
                map.get("userId"),
                null
        );
        CommentVO commentVO = new CommentVO(
                //获取UUID并转化为String对象
                UUID.randomUUID().toString().replace("-",""),
                map.get("invitationId"),
                cid,
                null
        );
        try{
            int i = commentMapper.insertOneComment(commentPO);
            if (i==1){
                int j = commentMapper.insertMidOneComment(commentVO);
                if (j==1){
                    map.put("commentNumber",Integer.parseInt(map.get("commentNumber"))+1+"");
                    int k = essayMapper.updateEssayNumber(map);
                    if (k == 1){
                        return ResultEntity.SUCCESS;
                    }
                }
            }

        }catch (Exception e){
            return e.getMessage();
        }
        return ResultEntity.FALSE;
    }

    @Override
    public PageInfo<CommentPO> getAllComment(HashMap<String, String> map) {
        try{
            int currentPage = Integer.parseInt(map.get("currentPage"));
            // 页面条数
            int pageSize = Integer.parseInt(map.get("pageSize"));
            PageHelper.startPage(currentPage,pageSize);
            List<CommentPO> commentPOS = commentMapper.selectNoParentComment(map);
            for (CommentPO tmp:
                    commentPOS) {
                UserPO userPO = new UserPO();
                userPO.setImageUrl(tmp.getUserPO().getImageUrl());
                userPO.setNickName(tmp.getUserPO().getNickName());
                tmp.setUserPO(userPO);
            }
            PageInfo<CommentPO> commentPOPageInfo = new PageInfo<>(commentPOS);
            return commentPOPageInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public String  addReOneComment(HashMap<String, String> map) {
        String cid = UUID.randomUUID().toString().replace("-","");
        CommentPO commentPO = new CommentPO(
                cid,
                map.get("parentId"),
                "",
                map.get("content"),
                0,
                0,
                0,
                new Date(),
                1,
                map.get("userId"),
                null
        );
        try{
            int i = commentMapper.insertOneComment(commentPO);
            if (i==1){
                map.put("commentNumber",Integer.parseInt(map.get("commentNumber"))+1+"");
                int k = commentMapper.updateCommentNumber(map);
                if (k == 1){
                    return ResultEntity.SUCCESS;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return ResultEntity.FALSE;
    }

    @Override
    public String likeOneComment(HashMap<String, String> map) {
        try {
            if (map.get("likeNumber") != null) {
                List<LikeCommentVO> likeCommentVO = commentMapper.selectLikeComment(map);
                if (likeCommentVO.size()> 0) {
                    map.put("likeNumber", Integer.parseInt(map.get("likeNumber")) - 1 + "");
                    int i = commentMapper.deleteLikeComment(map);
                    if (i != 1) {
                        return ResultEntity.FALSE;
                    }
                } else {
                    map.put("likeNumber", Integer.parseInt(map.get("likeNumber")) + 1 + "");
                    LikeCommentVO likeCommentVO1 = new LikeCommentVO(
                            UUID.randomUUID().toString().replace("-", ""),
                            map.get("commentId"),
                            map.get("userId")
                    );
                    int i = commentMapper.insertLikeComment(likeCommentVO1);
                    if (i != 1) {
                        return ResultEntity.FALSE;
                    }
                }
            }

            int q = commentMapper.updateCommentNumber(map);
            if (q == 1) {
                return ResultEntity.SUCCESS;
            }
        }catch(Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return ResultEntity.FALSE;
    }
    @Override
    public ArrayList<CommentPO> getAllReComment(HashMap<String, String> map) {
        try{
            List<CommentPO> commentPOS = commentMapper.selectParentComment(map);
            for (CommentPO tmp:
            commentPOS) {
                UserPO userPO = new UserPO();
                userPO.setImageUrl(tmp.getUserPO().getImageUrl());
                userPO.setNickName(tmp.getUserPO().getNickName());
                tmp.setUserPO(userPO);
            }
            return (ArrayList<CommentPO>) commentPOS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
