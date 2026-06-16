package com.jxau.mapper;

import com.jxau.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author cheng
 */
public interface EssayMapper {
    /**
     * 插入一篇文章
     * @return 插入条数
     * @param essay
     */
    int insertOneEssay(Essay essay);

    /**
     * 删除文章 update 修改状态为 1 删除标记
     * @param list
     * @return
     */
    int deleteEssay(ArrayList<String>list);

    /**
     * 查询所有文章（测试）
     * @return
     */
    List<Essay> selectAllEssaysTest();

    /**
     * 查询符合要求的文章
     *
     * @param map
     * @return
     */
    List<Essay> selectAllEssays(HashMap<String, String> map);

    /**
     * 查询文章时使用的查询文章对应用户方法
     * @param id
     * @return
     */
    UserPO getUserByUserId(@Param("id")String id);

    /**
     * 用户修改文章
     * @param map
     * @return
     */
    int updateEssay(HashMap<String, String> map);

    /**
     * 用户添加文章图片
     * @param imagePo
     * @return
     */
    int addImage(ImagePo imagePo);

    /**
     * 查询文章图片
     * @param iid
     * @return
     */
    List<ImagePo> selectImages(@Param("iid") String iid);

    /**
     * 文章评论数+1或者文章点赞数＋1或者文章访问数+1
     * @param map
     * @return
     */
    int updateEssayNumber(HashMap<String, String> map);

    /**
     * 用户点赞中间表
     * @param map
     * @return
     */
    LikeEssayVO selectLikeEssays(HashMap<String, String> map);

    /**
     * 用户取消点赞
     * @param map
     * @return
     */
    int deleteLikeEssays(HashMap<String, String> map);

    /**
     * 用户点赞
     * @param likeEssayVO
     * @return
     */
    int insertLikeEssays(LikeEssayVO likeEssayVO);

    /**
     * 查看一篇文章
     * @param map
     * @return
     */
    Essay selectOneEssays(HashMap<String, String> map);

    /**
     * 给用户推荐文章
     * @param list
     * @return
     */
    List<Essay> selectEssays(@Param("idList") List<String> list);

    List<EssayFileVO> selectFileEssays();

    void insertUserViewHistory(UserHistoryPO userHistoryPO);
}
