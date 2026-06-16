package com.jxau.service;

import com.github.pagehelper.PageInfo;
import com.jxau.pojo.ResourcePO;
import com.jxau.pojo.VideoPO;
import com.jxau.util.ResultEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @author chenzouquan
 */
public interface ResourceService {
    /**
     * 用户上传一个资源
     * @param map
     * @param files
     * @return
     */
    String addOneResource(HashMap<String, String> map, MultipartFile[] files);

    /**
     * 用户查看所有的资源
     * @param map
     * @return
     */
    PageInfo<ResourcePO> selectAllResource(HashMap<String, String> map);

    /**
     * 用户删除资源
     * @param map
     * @return
     */
    String deleteResource(HashMap<String, Object> map);

    /**
     * 用户点赞资源
     * @param map
     * @return
     */
    String likeOneResource(HashMap<String, String> map);

    /**
     * 用户查看视频
     * @param map
     * @return
     */
    PageInfo<VideoPO> selectAllVideo(HashMap<String, String> map);

    /**
     * 用户观看视频
     * @param map
     * @return
     */
    String addOneLook(HashMap<String, String> map);
}
