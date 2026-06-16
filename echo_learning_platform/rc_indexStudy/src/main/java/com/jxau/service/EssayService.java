package com.jxau.service;


import com.github.pagehelper.PageInfo;
import com.jxau.pojo.Essay;
import com.jxau.pojo.EssayFileVO;
import com.jxau.pojo.LikeEssayVO;
import com.jxau.util.ResultEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * 文章业务标签
 * @author cheng
 */
public interface EssayService {

    /**
     * 添加一篇文章
     * @param map
     * @return 操作信息
     */
    ResultEntity<String> addOneEssay(HashMap<String,Object> map);

    /**
     * 用户删除一篇或者多篇的文章
     * @param map
     * @return 操作信息
     */
    String deleteEssay(HashMap<String, Object> map);

    /**
     * 用户按要求查询文章（未进行推荐算法）
     * @param map
     * @return
     */
    PageInfo selectAllEssays(HashMap<String, String> map);

    /**
     * 用户修改文章
     * @param map
     * @return
     */
    String updateEssay(HashMap<String, String> map);

    /**
     * 用户给文章点赞
     * @param map
     * @return
     */
    String updateEssayNumber(HashMap<String, String> map);

    /**
     * 用户查看一篇文章
     * @param map
     * @return
     */
    Essay selectOneEssays(HashMap<String, String> map);

    /**
     * 查看用户是否点赞
     * @return
     */
    LikeEssayVO selectOneLikeEassy(HashMap<String, String> map);

    /**
     * 查看用户点赞过的评论
     * @return
     */
    String[] selectAllLikeComment(HashMap<String, String> map);

    String addOneImage(String invitationId, MultipartFile files,boolean flag);

    /**
     * 推荐算法
     * @param map
     * @return
     */
    List<Essay> selectAllEssaysRecommend(HashMap<String, String> map);

    /**
     * 查看最乐文章
     * @return
     */
    List<EssayFileVO> selectFileEssays();

    void insertHisttoryView(String userId, String invitationId);
}
