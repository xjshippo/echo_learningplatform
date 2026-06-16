package com.jxau.mapper;

import com.jxau.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 资源接口类
 * @author chenzouquan
 */
public interface ResourceMapper {

    /**
     * 插入资源
     * @param resourcePO
     * @return
     */
    int insertOneResource(ResourcePO resourcePO);

    /**
     * 查看所有资源
     * @param map
     * @return
     */
    List<ResourcePO> selectAllResource(HashMap<String, String> map);

    /**
     * 删除一个或者多个资源
     * @param list
     * @return
     */
    int deleteResource(ArrayList<String> list);

    /**
     * 访问数和评论数和点赞数加1
     * @param map
     * @return
     */
    int updateResourceNumber(HashMap<String, String> map);

    /**
     * 查看用户点赞
     * @param map
     * @return
     */
    LikeResourceVO selectLikeResource(HashMap<String, String> map);

    /**
     * 用户取消点赞
     * @param map
     * @return
     */
    int deleteLikeResource(HashMap<String, String> map);

    /**
     * 用户点赞
     * @param likeResourceVO1
     * @return
     */
    int insertLikeResource(LikeResourceVO likeResourceVO1);

    /**
     * 用户查看所有的视频
     * @param map
     * @return
     */
    List<VideoPO> selectAllVideo(HashMap<String, String> map);

    /**
     * 查看多少个用户看过
     * @param id
     * @return
     */
    List<LookUserPo> selectLookAllUser(@Param("videoId") String id);

    /**
     * 用户观看视频
     * @param lookUserPo
     * @return
     */
    int insertOneLook(LookUserPo lookUserPo);
}
