package com.jxau.service.impl;

import com.aliyun.oss.OSS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;
import com.jxau.config.OSSProperties;
import com.jxau.mapper.CommentMapper;
import com.jxau.mapper.EssayMapper;
import com.jxau.mapper.UserMapper;
import com.jxau.pojo.*;
import com.jxau.service.EssayService;
import com.jxau.util.JsonResult;
import com.jxau.util.OssUtil;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * EssayService 实现类
 * @author cheng
 */
@Service
public class EssayServiceImpl implements EssayService {

    @Autowired
    private EssayMapper essayMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<String> addOneEssay(HashMap<String, Object> map) {
        String eid = UUID.randomUUID().toString().replace("-","");

        //ArrayList<String> tag = (ArrayList<String>)map.get("tags");
        Object tags = map.get("tags");

        if(tags instanceof ArrayList){
            ArrayList<String> tag = (ArrayList<String>)map.get("tags");

            String replace = tag.toString().replace("[", "").replace("]", "");
            String headUrl = "https://jxau7124.oss-cn-shenzhen.aliyuncs.com/20211113/25062eca757b41e9a209e53d0b381424.jpg";
            Essay essay = new Essay(
                    eid,
                    (String) map.get("title"),
                    (String) map.get("content"),
                    null,
                    (String) map.get("userId"),
                    replace,
                    null,
                    // 0 审核中
                    1,
                    0,
                    0,
                    0,
                    new Date(),
                    headUrl,
                    null
            );
            try {
                int i = essayMapper.insertOneEssay(essay);
                if (i != 1) {
                    return ResultEntity.falseWithoutData("失败");
                }
            }catch (Exception e){
                e.printStackTrace();
                return ResultEntity.falseWithoutData(e.getMessage());
            }
            return ResultEntity.successWithData(eid);
        }else{
            return ResultEntity.falseWithoutData("参数接收失败！");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addOneImage(String invitationId, MultipartFile file,boolean flag) {
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
                    imagePo.setIid(invitationId);
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
            if (flag){
                HashMap<String,String> map = new HashMap<>();
                map.put("invitationId",invitationId);
                map.put("coverImgUrl",imagePo.getUrl());
                int i = essayMapper.updateEssay(map);
                if (i != 1) {
                    return ResultEntity.FALSE;
                }
            }
            int i = essayMapper.addImage(imagePo);
            if (i != 1) {
                return ResultEntity.FALSE;
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.FALSE;
        }
        return ResultEntity.SUCCESS;
    }

    @Override
    public List<Essay> selectAllEssaysRecommend(HashMap<String, String> map) {

        try{
            // 当前用户向量
            UserProfessionalVO userNew = null; int userNum = 0;
            // 文章矩阵
            HashMap<String,Integer> essayMap = new HashMap<>(); int eI = 0;
            HashMap<String,Integer> userMap = new HashMap<>(); int uI = 0;
            // 查询用户数据
            List<UserProfessionalVO> list = userMapper.selectUserProfessional();
            // 无用户数据时回退到普通搜索
            if (list == null || list.isEmpty()) {
                return essayMapper.selectAllEssays(map);
            }
            for (UserProfessionalVO userProfessionalVO : list) {

                List<String> likeInvitationIds = userMapper.selectLikeInvitationIds(userProfessionalVO.getUserId());
                for (String tmpId: likeInvitationIds) {
                    essayMap.put(tmpId,eI);
                    eI ++;
                }
                userMap.put(userProfessionalVO.getUserId(),uI);
                if (userProfessionalVO.getUserId().equals(map.get("userId"))){
                    userNew = userProfessionalVO;
                    userNum = uI;
                }
                uI++;
                userProfessionalVO.setInvitationId(likeInvitationIds);
            }
            // 根据用户信息来解决用户冷启动缺少判断条件的问题
            if (userNew != null && userNew.getInvitationId() == null){
                String professional = userNew.getProfessional();
                map.put("title",professional); map.put("content",professional);
                List<Essay> essays = essayMapper.selectAllEssays(map);
                essays.addAll(essayMapper.selectAllEssays(new HashMap<>()));
                return essays;
            }else{
                // 构建用户矩阵
                int[][] numbers = new int[uI+1][eI+1];

                for (UserProfessionalVO userProfessionalVO : list) {
                    for (String tmpEssay: userProfessionalVO.getInvitationId()) {
                        numbers[userMap.get(userProfessionalVO.getUserId())][essayMap.get(tmpEssay)] = 1;
                    }
                }
                // 利用余弦公式计算用户之间的相似系数
                double[] users = new double[list.size()];
                // 记录最大系数对应的用户位置
                TreeMap<Integer,Double> maxUser = new TreeMap<>();
                for (int i = 0; i < list.size(); i++) {
                    if (i == userNum){
                        continue;
                    }
                    int a = 1;int b = 1;int sum = 0;
                    for (int j = 0; j < essayMap.size(); j++) {
                        sum += numbers[userNum][j] * numbers[i][j];
                        a += numbers[userNum][j]*numbers[userNum][j];
                        b += numbers[i][j]*numbers[i][j];
                    }
                    users[i] = sum/Math.sqrt(a*b);
                    maxUser.put(i,users[i]);
                }
                List<String> invitationId = list.get(maxUser.lastKey()).getInvitationId();
                if (invitationId != null && invitationId.size()>0){
                    List<Essay> essays = essayMapper.selectEssays(invitationId);
                    essays.addAll(essayMapper.selectAllEssays(new HashMap<>()));
                    for (Essay e: essays) {
                        if (e.getTags() != null && !e.getTags().isEmpty()) {
                            e.setTag(e.getTags().split(","));
                        }
                        if (e.getUser() != null) {
                            UserPO userPO = new UserPO();
                            userPO.setImageUrl(e.getUser().getImageUrl());
                            userPO.setNickName(e.getUser().getNickName());
                            e.setUser(userPO);
                        }
                    }
                    return essays;
                }else if (userNew != null) {
                    String professional = userNew.getProfessional();
                    map.put("title",professional); map.put("content",professional);
                    List<Essay> essays = essayMapper.selectAllEssays(map);
                    essays.addAll(essayMapper.selectAllEssays(new HashMap<>()));
                    for (Essay e: essays) {
                        if (e.getTags() != null && !e.getTags().isEmpty()) {
                            e.setTag(e.getTags().split(","));
                        }
                        if (e.getUser() != null) {
                            UserPO userPO = new UserPO();
                            userPO.setImageUrl(e.getUser().getImageUrl());
                            userPO.setNickName(e.getUser().getNickName());
                            e.setUser(userPO);
                        }
                    }
                    return essays;
                } else {
                    return essayMapper.selectAllEssays(map);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EssayFileVO> selectFileEssays() {
        int currentPage = 1;
        // 页面条数
        int pageSize = 1000;
        PageHelper.startPage(currentPage,pageSize);
        List<EssayFileVO> list = essayMapper.selectFileEssays();
        if (list.size() > 10 ){
            return list.subList(0,10);
        }else{
            return list;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteEssay(HashMap<String, Object> map) {
        try{
            ArrayList<String> essayId = (ArrayList<String>) map.get("essayId");
            int end = essayMapper.deleteEssay(essayId);
            if (end == essayId.size()){
                return ResultEntity.SUCCESS;
            }else{
                return ResultEntity.FALSE;
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public PageInfo<Essay> selectAllEssays(HashMap<String, String> map) {
        try{
            if(map.get("title")!= null){
                map.put("currentPage","1");
                map.put("pageSize","100");
            }
            int currentPage = Integer.parseInt(map.get("currentPage"));
            // 页面条数
            int pageSize = Integer.parseInt(map.get("pageSize"));
            PageHelper.startPage(currentPage,pageSize);
            List<Essay> essays = essayMapper.selectAllEssays(map);
            for (Essay e: essays) {
                if (e.getTags() != null && !e.getTags().isEmpty()) {
                    e.setTag(e.getTags().split(","));
                }
                if (e.getUser() != null) {
                    UserPO userPO = new UserPO();
                    userPO.setImageUrl(e.getUser().getImageUrl());
                    userPO.setNickName(e.getUser().getNickName());
                    e.setUser(userPO);
                }
            }
            PageInfo<Essay> essayPageInfo = new PageInfo<>(essays);
            return essayPageInfo;
        }catch (Exception e){
           e.printStackTrace();
        }
       return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateEssay(HashMap<String, String> map) {
        try{
            int end = essayMapper.updateEssay(map);
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
    @Transactional(rollbackFor = Exception.class)
    public String updateEssayNumber(HashMap<String, String> map) {
        try{
            if (map.get("likeNumber") != null){
                LikeEssayVO likeEssayVO = essayMapper.selectLikeEssays(map);
                if (likeEssayVO != null){
                    map.put("likeNumber",Integer.parseInt(map.get("likeNumber"))-1+"");
                    int i = essayMapper.deleteLikeEssays(map);
                    if (i != 1){
                        return ResultEntity.FALSE;
                    }
                }else {
                    map.put("likeNumber",Integer.parseInt(map.get("likeNumber"))+1+"");
                    LikeEssayVO likeEssayVO1 = new LikeEssayVO(
                            UUID.randomUUID().toString().replace("-",""),
                            map.get("invitationId"),
                            map.get("userId")
                    );
                    int i = essayMapper.insertLikeEssays(likeEssayVO1);
                    if (i != 1){
                        return ResultEntity.FALSE;
                    }
                }
            }

            int end = essayMapper.updateEssayNumber(map);
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
    @Transactional(rollbackFor = Exception.class)
    public Essay selectOneEssays(HashMap<String, String> map) {
        Essay essay = essayMapper.selectOneEssays(map);
        if (essay == null) {
            return null;
        }
        if (essay.getImages() == null) {
            essay.setImages(new java.util.ArrayList<>());
        }
        if (essay.getImages().size() == 0){
            essay.getImages().add(new ImagePo(
                UUID.randomUUID().toString().replace("-",""),
                essay.getId(),
                essay.getCoverImgUrl()
            ));
        }
        if (essay.getTags() != null && !essay.getTags().isEmpty()) {
            essay.setTag(essay.getTags().split(","));
        }
        if (essay.getUser() != null) {
            UserPO userPO = new UserPO();
            userPO.setImageUrl(essay.getUser().getImageUrl());
            userPO.setNickName(essay.getUser().getNickName());
            essay.setUser(userPO);
        }
        return essay;
    }

    @Override
    public LikeEssayVO selectOneLikeEassy(HashMap<String,String> map) {
        LikeEssayVO likeEssayVO = essayMapper.selectLikeEssays(map);
        return likeEssayVO;
    }

    @Override
    public String[] selectAllLikeComment(HashMap<String,String> map) {

        List<LikeCommentVO> likeCommentVOs = commentMapper.selectLikeComment(map);
        String[] commentId = new String[likeCommentVOs.size()];
        for (int i = 0; i < commentId.length; i++) {
            commentId[i] = likeCommentVOs.get(i).getCId();
        }
        return commentId;
    }

    @Override
    public void insertHisttoryView(String userId, String invitationId) {
        // 用户历史浏览记录
        UserHistoryPO userHistoryPO = new UserHistoryPO();
        userHistoryPO.setCreateTime(new Date());
        userHistoryPO.setUserId(userId);
        userHistoryPO.setViewId(invitationId);
        userHistoryPO.setId(UUID.randomUUID().toString().replace("-",""));

        essayMapper.insertUserViewHistory(userHistoryPO);
    }
}
