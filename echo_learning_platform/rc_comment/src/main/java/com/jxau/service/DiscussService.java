package com.jxau.service;

import com.github.pagehelper.PageInfo;
import com.jxau.pojo.DiscussCommentPO;
import com.jxau.pojo.DiscussPO;
import com.jxau.pojo.TypePO;
import com.jxau.pojo.UserPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;


public interface DiscussService {
    /**
     * 添加一个讨论区
     * @param map
     * @return
     */
    String addOneDiscuss(HashMap<String, String> map, MultipartFile file);

    /**
     * 用户关注一个讨论区
     * @param map
     * @return
     */
    String addOneLikeDiscuss(HashMap<String, String> map);

    /**
     * 查看用户关注的讨论区
     * @param map
     * @return
     */
    PageInfo<DiscussPO> getUserLikeDiscuss(HashMap<String, String> map);

    /**
     * 查看所有的讨论区
     * @param map
     * @return
     */
    PageInfo<DiscussPO> getAllDiscuss(HashMap<String, String> map);

    /**
     * 用户取消关注
     * @param map
     * @return
     */
    String unOneLikeDiscuss(HashMap<String, String> map);

    /**
     * 用户发表帖子
     * @param map
     * @return
     */
    String addOneDiscussComment(HashMap<String, String> map);

    /**
     * 用户点赞帖子
     * @param map
     * @return
     */
    String likeOneDiscussComment(HashMap<String, String> map);

    /**
     * 用户回复评论
     * @param map
     * @return
     */
    String addReOneDiscussComment(HashMap<String, String> map);

    /**
     * 用户查看所有帖子
     * @param map
     * @return
     */
    PageInfo<DiscussCommentPO> getAllDiscussComment(HashMap<String, String> map);


    /**
     * 用户查看所有的类别
     * @param map
     * @return
     */
    PageInfo<TypePO> getAllType(HashMap<String, String> map);

    /**
     * 用户查看一篇帖子
     * @param map
     * @return
     */
    DiscussCommentPO selectOneDiscussComment(HashMap<String, String> map);

    /**
     * 用户点赞过的帖子
     * @param map
     * @return
     */
    String[] selectAllLikeComment(HashMap<String, String> map);

    /**
     * 用户添加图片
     * @param commentId
     * @param file
     * @return
     */
    String addOneImage(String commentId, MultipartFile file);
}
