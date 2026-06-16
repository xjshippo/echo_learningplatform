package com.jxau.mapper;

import com.jxau.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 讨论区mapper类
 *
 */
public interface DiscussMapper {
    /**
     * 插入一个讨论区
     * @param discussPO
     * @return
     */
    int insertOneDiscuss(DiscussPO discussPO);

    /**
     * 插入一条关注信息
     * @param discussVO
     * @return
     */
    int insertOneLikeDiscuss(DiscussVO discussVO);

    /**
     * 增加关注人数
     * @param map
     * @return
     */
    int updateLikeNumber(HashMap<String, String> map);

    /**
     * 根据id查找讨论区
     * @param id
     * @return
     */
    DiscussPO getOneDiscussById(String id);

    /**
     * 查看用户关注的讨论区
     * @param map
     * @return
     */
    List<DiscussVO> selectUserLikeDiscuss(HashMap<String, String> map);

    /**
     * 查看所有的讨论区
     * @param map
     * @return
     */
    List<DiscussPO> selectAllDiscuss(HashMap<String, String> map);


    /**
     * 用户取消关注讨论区
     * @param map
     * @return
     */
    int deleteOneLikeDiscuss(HashMap<String, String> map);

    /**
     * 用户发表帖子
     * @param discussCommentPO
     * @return
     */
    int insertOneDiscussComment(DiscussCommentPO discussCommentPO);

    /**
     * 用户取消点赞
     * @param map
     * @return
     */
    int deleteLikeEssays(HashMap<String, String> map);

    /**
     * 用户点赞
     * @param likeDiscussCommentPO1
     * @return
     */
    int insertLikeEssays(LikeDiscussCommentPO likeDiscussCommentPO1);

    /**
     * 查看用户是否点赞
     * @param map
     * @return
     */
    List<LikeDiscussCommentPO> selectLikeEssays(HashMap<String, String> map);

    /**
     * 修改文章点赞数/观看数/评论数
     * @param map
     * @return
     */
    int updateDiscussCommentNumber(HashMap<String, String> map);

    /**
     * 用户查看所有的帖子
     * @param map
     * @return
     */
    List<DiscussCommentPO> selectAllDiscussComment(HashMap<String, String> map);

    /**
     * 查询帖子时使用的查询对应用户方法
     * @param id
     * @return
     */
    UserPO getUserByUserId(@Param("id")String id);

    /**
     * 用户查看所有的种类
     * @param map
     * @return
     */
    List<TypePO> selectAllType(HashMap<String, String> map);

    /**
     * 查看一个帖子
     * @param map
     * @return
     */
    DiscussCommentPO selectOneDiscussComment(HashMap<String, String> map);

    /**
     * 查看讨论的帖子的图片
     */
    List<ImagePo> selectAllImage(String id);

    /**
     * 用户插入图片
     * @param imagePo
     * @return
     */
    int addImage(ImagePo imagePo);
}
