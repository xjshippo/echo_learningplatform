package com.jxau.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxau.config.OSSProperties;
import com.jxau.mapper.ResourceMapper;
import com.jxau.pojo.*;
import com.jxau.service.ResourceService;
import com.jxau.util.OssUtil;
import com.jxau.util.ResultEntity;
import com.jxau.util.VideoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 文件上传
 * @author chenzouquan
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private OSSProperties ossProperties;
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public String addOneResource(HashMap<String, String> map, MultipartFile[] files) {
        String imageUrl = "";
        String url = "";
        if (files != null && files.length ==2){
            try{
                ResultEntity<String> stringResultEntity = OssUtil.uploadFileToOss(ossProperties.getEndPoint(),
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        files[0].getInputStream(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        files[0].getOriginalFilename());
                if (ResultEntity.SUCCESS.equals(stringResultEntity.getResult())){
                    imageUrl = "HTTP://" + stringResultEntity.getData();
                    if (map.get("type").equals("video")){
                        String videoId = VideoUtil.uploadVideoAly(files[1]);
                        if (videoId != null){
                            DefaultAcsClient client = VideoUtil.initVodClient();
                            url = VideoUtil.getPlayInfo(client,videoId);
                            if (url == null){
                                return "文件上传失败";
                            }
                        }else {
                            return "资源上传失败";
                        }
                    }else{
                        stringResultEntity = OssUtil.uploadFileToOss(ossProperties.getEndPoint(),
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        files[1].getInputStream(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        files[1].getOriginalFilename());
                        if (ResultEntity.SUCCESS.equals(stringResultEntity.getResult())){
                            url = "HTTP://" + stringResultEntity.getData();
                        }else{
                            return "资源上传失败";
                        }
                    }
                }else{
                    return "封面上传失败";
                }
            }catch (Exception e){
                e.printStackTrace();
                return e.getMessage();
            }
        }
        ResourcePO resourcePO = new ResourcePO(
                UUID.randomUUID().toString().replace("-",""),
                map.get("content"),
                map.get("userId"),
                null,
                url,
                imageUrl,
                map.get("type"),
                map.get("tags"),
                null,
                new Date(),
                0,
                0,
                0,
                // 暂时没有后台管理系统，所以默认通过
                1,
                map.get("size")
        );
        int i = resourceMapper.insertOneResource(resourcePO);
        if (i != 1) {
            return ResultEntity.FALSE;
        }

        return ResultEntity.SUCCESS;
    }

    @Override
    public PageInfo<ResourcePO> selectAllResource(HashMap<String, String> map) {
        try{
            String cp = map.get("currentPage");
            String ps = map.get("pageSize");
            int currentPage = (cp != null) ? Integer.parseInt(cp) : 1;
            int pageSize = (ps != null) ? Integer.parseInt(ps) : 10;
            PageHelper.startPage(currentPage,pageSize);
            List<ResourcePO> resourcePOS = resourceMapper.selectAllResource(map);
            if (resourcePOS != null) {
                for (ResourcePO e: resourcePOS) {
                    if (e.getTags() != null && !e.getTags().isEmpty()) {
                        e.setTag(e.getTags().split(","));
                    }
                }
            }
            PageInfo<ResourcePO> resourcePOPageInfo = new PageInfo<>(resourcePOS);
            return resourcePOPageInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteResource(HashMap<String, Object> map) {
        try{
            ArrayList<String> resourceId = (ArrayList<String>) map.get("resourceId");
            int end = resourceMapper.deleteResource(resourceId);
            if (end == resourceId.size()){
                return ResultEntity.SUCCESS;
            }else{
                return ResultEntity.FALSE;
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public String likeOneResource(HashMap<String, String> map) {
        try{
            if (map.get("likeNumber") != null){
                LikeResourceVO likeResourceVO = resourceMapper.selectLikeResource(map);
                if (likeResourceVO != null){
                    map.put("likeNumber",Integer.parseInt(map.get("likeNumber"))-1+"");
                    int i = resourceMapper.deleteLikeResource(map);
                    if (i != 1){
                        return ResultEntity.FALSE;
                    }
                }else {
                    map.put("likeNumber",Integer.parseInt(map.get("likeNumber"))+1+"");
                    LikeResourceVO likeResourceVO1 = new LikeResourceVO(
                            UUID.randomUUID().toString().replace("_",""),
                            map.get("invitationId"),
                            map.get("userId")
                    );
                    int i = resourceMapper.insertLikeResource(likeResourceVO1);
                    if (i != 1){
                        return ResultEntity.FALSE;
                    }
                }
            }

            int end = resourceMapper.updateResourceNumber(map);
            if (end == 1){
                return ResultEntity.SUCCESS;
            }else{
                return ResultEntity.FALSE;
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public PageInfo<VideoPO> selectAllVideo(HashMap<String, String> map) {
        try{
            String cp = map.get("currentPage");
            String ps = map.get("pageSize");
            int currentPage = (cp != null) ? Integer.parseInt(cp) : 1;
            int pageSize = (ps != null) ? Integer.parseInt(ps) : 10;
            PageHelper.startPage(currentPage,pageSize);
            List<VideoPO> videoPOS = resourceMapper.selectAllVideo(map);

            PageInfo<VideoPO> videoPOPageInfo = new PageInfo<>(videoPOS);
            return videoPOPageInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String addOneLook(HashMap<String, String> map) {
        List<LookUserPo> videoId = resourceMapper.selectLookAllUser(map.get("videoId"));
        for (LookUserPo tmp: videoId) {
            if (tmp.getUid().equals(map.get("userId"))){
                return "用户曾经观看过";
            }
        }
        LookUserPo lookUserPo = new LookUserPo(
                UUID.randomUUID().toString().replace("-", ""), map.get("userId"), map.get("videoId"), null
        );
        try {
            int i = resourceMapper.insertOneLook(lookUserPo);
            if (i == 1){
                return ResultEntity.SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }
}
