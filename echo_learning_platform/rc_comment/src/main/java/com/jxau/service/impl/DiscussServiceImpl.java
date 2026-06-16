package com.jxau.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxau.config.OSSProperties;
import com.jxau.mapper.DiscussMapper;
import com.jxau.pojo.*;
import com.jxau.service.DiscussService;
import com.jxau.util.OssUtil;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 讨论区评论类
 *
 */
@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    OSSProperties ossProperties;
    @Autowired
    DiscussMapper discussMapper;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public String addOneDiscuss(HashMap<String, String> map, MultipartFile file) {
        try{
            ResultEntity<String> stringResultEntity = OssUtil.uploadFileToOss(ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    file.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    file.getOriginalFilename());
            if (ResultEntity.SUCCESS.equals(stringResultEntity.getResult())){
                DiscussPO discussPO = new DiscussPO(
                        UUID.randomUUID().toString().replace("_",""),
                        map.get("discuss_name"),
                        map.get("tags"),
                        new Date(),
                        "HTTP://" + stringResultEntity.getData(),
                        0
                );
                int i = discussMapper.insertOneDiscuss(discussPO);
                if (i != 1){
                    return ResultEntity.FALSE;
                }
                return ResultEntity.SUCCESS;
            }else{
                return "图片上传失败";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public String addOneLikeDiscuss(HashMap<String, String> map) {
        DiscussVO discussVO = new DiscussVO(
                UUID.randomUUID().toString().replace("-",""),
                map.get("userId"),
                map.get("discussId"),
                null
        );
        int i = discussMapper.insertOneLikeDiscuss(discussVO);
        if (i == 1){
           int j = discussMapper.updateLikeNumber(map);
           if (j== 1){
               return ResultEntity.SUCCESS;
           }
        }
        return ResultEntity.FALSE;
    }

    @Override
    public PageInfo<DiscussPO> getUserLikeDiscuss(HashMap<String, String> map) {
        try{
            int currentPage = Integer.parseInt(map.get("currentPage"));
            // 页面条数
            int pageSize = Integer.parseInt(map.get("pageSize"));
            PageHelper.startPage(currentPage,pageSize);
            List<DiscussVO> discussVOS = discussMapper.selectUserLikeDiscuss(map);
            List<DiscussPO> discussPOS = new ArrayList<>();
            for (DiscussVO tmp: discussVOS) {
                discussPOS.add(tmp.getDiscussPO());
            }
            PageInfo<DiscussPO> discussPOPageInfo = new PageInfo<>(discussPOS);
            return discussPOPageInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PageInfo<DiscussPO> getAllDiscuss(HashMap<String, String> map) {
        try{
            int currentPage = Integer.parseInt(map.get("currentPage"));
            // 页面条数
            int pageSize = Integer.parseInt(map.get("pageSize"));
            PageHelper.startPage(currentPage,pageSize);
            List<DiscussPO> discuss = discussMapper.selectAllDiscuss(map);
            PageInfo<DiscussPO> discussPOPageInfo = new PageInfo<>(discuss);
            return discussPOPageInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public String unOneLikeDiscuss(HashMap<String, String> map) {
        int i = discussMapper.deleteOneLikeDiscuss(map);
        if (i == 1){
            int j = discussMapper.updateLikeNumber(map);
            if (j == 1){
                return ResultEntity.SUCCESS;
            }else{
                return "修改次数失败";
            }
        }else{
            return "删除失败";
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public String addOneDiscussComment(HashMap<String, String> map) {
        DiscussCommentPO discussCommentPO = new DiscussCommentPO(
                UUID.randomUUID().toString().replace("_",""),
                map.get("title"),
                map.get("content"),
                map.get("discussId"),
                map.get("userId"),
                null,
                "@",
                0,
                0,
                0,
                new Date(),
                1,
                0,
                null
        );
        int i = discussMapper.insertOneDiscussComment(discussCommentPO);
        if(i == 1){
            return ResultEntity.SUCCESS;
        }
        return ResultEntity.FALSE;
    }

    @Override
    public String likeOneDiscussComment(HashMap<String, String> map) {
        if (map.get("likeNumber") != null){
            List<LikeDiscussCommentPO> likeDiscussCommentPO =  discussMapper.selectLikeEssays(map);
            if (likeDiscussCommentPO != null){
                map.put("likeNumber",Integer.parseInt(map.get("likeNumber"))-1+"");
                int i = discussMapper.deleteLikeEssays(map);
                if (i != 1){
                    return ResultEntity.FALSE;
                }
            }else {
                map.put("likeNumber",Integer.parseInt(map.get("likeNumber"))+1+"");
                LikeDiscussCommentPO likeDiscussCommentPO1 = new LikeDiscussCommentPO(
                        UUID.randomUUID().toString().replace("_",""),
                        map.get("commentId"),
                        map.get("userId")
                );
                int i = discussMapper.insertLikeEssays(likeDiscussCommentPO1);
                if (i != 1){
                    return ResultEntity.FALSE;
                }
            }
        }

        int end = discussMapper.updateDiscussCommentNumber(map);
        if (end == 1){
            return ResultEntity.SUCCESS;
        }else{
            return ResultEntity.FALSE;
        }
    }

    @Override
    public String addReOneDiscussComment(HashMap<String, String> map) {
        DiscussCommentPO discussCommentPO = new DiscussCommentPO(
                UUID.randomUUID().toString().replace("_",""),
                "",
                map.get("content"),
                "",
                map.get("userId"),
                null,
                map.get("parentId"),
                0,
                0,
                0,
                new Date(),
                1,
                0,
                null
        );
        int i = discussMapper.insertOneDiscussComment(discussCommentPO);
        if(i == 1){
            map.put("commentId",map.get("parentId"));
            map.put("commentNumber",Integer.parseInt(map.get("commentNumber"))+1+"");
            discussMapper.updateDiscussCommentNumber(map);
            return ResultEntity.SUCCESS;
        }
        return ResultEntity.FALSE;
    }

    @Override
    public PageInfo<DiscussCommentPO> getAllDiscussComment(HashMap<String, String> map) {
        try{
            int currentPage = Integer.parseInt(map.get("currentPage"));
            // 页面条数
            int pageSize = Integer.parseInt(map.get("pageSize"));
            PageHelper.startPage(currentPage,pageSize);
            if (map.get("parentId") == null){
                map.put("parentId","@");
            }
            List<DiscussCommentPO> discussCommentPOS = discussMapper.selectAllDiscussComment(map);
            for (DiscussCommentPO e: discussCommentPOS) {
                UserPO userPO = new UserPO();
                userPO.setImageUrl(e.getUser().getImageUrl());
                userPO.setNickName(e.getUser().getNickName());
                e.setUser(userPO);
            }
            PageInfo<DiscussCommentPO> discussCommentPOPageInfo = new PageInfo<>(discussCommentPOS);
            return discussCommentPOPageInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PageInfo<TypePO> getAllType(HashMap<String, String> map) {
        try {
            int currentPage = Integer.parseInt(map.get("currentPage"));
            // 页面条数
            int pageSize = Integer.parseInt(map.get("pageSize"));
            PageHelper.startPage(currentPage, pageSize);
            List<TypePO> discussCommentPOS = discussMapper.selectAllType(map);
            PageInfo<TypePO> discussCommentPOPageInfo = new PageInfo<>(discussCommentPOS);
            return discussCommentPOPageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public DiscussCommentPO selectOneDiscussComment(HashMap<String, String> map) {
        DiscussCommentPO discussCommentPO = discussMapper.selectOneDiscussComment(map);
        return discussCommentPO;
    }

    @Override
    public String[] selectAllLikeComment(HashMap<String, String> map) {
        List<LikeDiscussCommentPO> likeDiscussCommentPOS = discussMapper.selectLikeEssays(map);
        String[] commentId = new String[likeDiscussCommentPOS.size()];
        for (int i = 0; i < commentId.length; i++) {
            commentId[i] = likeDiscussCommentPOS.get(i).getDcId();
        }
        return commentId;
    }

    @Override
    public String addOneImage(String commentId, MultipartFile file) {
        ImagePo imagePo = new ImagePo();
        if (file != null ){
            try{
                ResultEntity<String> stringResultEntity = OssUtil.uploadFileToOss(ossProperties.getEndPoint(),
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        file.getInputStream(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        file.getOriginalFilename());
                if (ResultEntity.SUCCESS.equals(stringResultEntity.getResult())){
                    // 如果上传成功就保存路径
                    imagePo = new ImagePo();
                    imagePo.setId(
                            UUID.randomUUID().toString().replace("-","")
                    );
                    imagePo.setIid(commentId);
                    imagePo.setUrl("https://" + stringResultEntity.getData());
                }else{
                    return "图片上传失败";
                }
            }catch (Exception e){
                e.printStackTrace();
                return e.getMessage();
            }
        }
        try {

            int i = discussMapper.addImage(imagePo);
            if (i != 1) {
                return ResultEntity.FALSE;
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.FALSE;
        }

        return ResultEntity.SUCCESS;
    }
}
