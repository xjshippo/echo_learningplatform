package com.jxau.service;


import com.github.pagehelper.PageInfo;
import com.jxau.pojo.CommentPO;
import com.sun.org.apache.bcel.internal.generic.ALOAD;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 评论使用
 * @author chenzouquan
 */
public interface CommentService {

    /**
     * 用户评论文章
     * @param map
     * @return
     */
    String addOneComment(HashMap<String, String> map);

    /**
     * 查看帖子评论
     * @param map
     * @return
     */
    PageInfo<CommentPO> getAllComment(HashMap<String, String> map);

    /**
     * 用户回复评论
     * @param map
     * @return
     */
    String addReOneComment(HashMap<String, String> map);

    /**
     * 用户点赞评论
     * @param map
     * @return
     */
    String likeOneComment(HashMap<String, String> map);

    /**
     * 查看评论回复
     * @param map
     * @return
     */
    ArrayList<CommentPO> getAllReComment(HashMap<String, String> map);
}
