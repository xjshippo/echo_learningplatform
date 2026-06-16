package com.jxau.mapper;

import com.jxau.pojo.CommentPO;
import com.jxau.pojo.CommentVO;
import com.jxau.pojo.LikeCommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 评论数据库接口
 * @author chenzouquan
 */
public interface CommentMapper {
    /**
     * 评论视图插入中间表
     * @param commentVO
     * @return
     */
    int insertMidOneComment(CommentVO commentVO);

    /**
     * 评论插入
     * @param commentPO
     * @return
     */
    int insertOneComment(CommentPO commentPO);

    /**
     * 查看所有的一级评论
     * @param map
     * @return
     */
    List<CommentPO> selectNoParentComment(HashMap<String, String> map);

    /**
     * 根据ID查询一个评论
     * @param cid
     * @return
     */
    CommentPO getOneCommentByCid(String cid);

    /**
     * 评论评论数+1或者评论点赞数＋1或者评论访问数+1
     * @param map
     * @return
     */
    int updateCommentNumber(HashMap<String, String> map);

    /**
     * 查看回复
     * @param map
     * @return
     */
    List<CommentPO> selectParentComment(HashMap<String, String> map);

    /**
     * 查看用户是否点赞
     * @param map
     * @return
     */
    List<LikeCommentVO> selectLikeComment(HashMap<String, String> map);

    /**
     * 用户取消点赞
     * @param map
     * @return
     */
    int deleteLikeComment(HashMap<String, String> map);

    /**
     * 用户点赞
     * @param likeCommentVO1
     * @return
     */
    int insertLikeComment(LikeCommentVO likeCommentVO1);
}
