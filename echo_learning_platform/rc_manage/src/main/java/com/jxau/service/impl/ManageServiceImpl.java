package com.jxau.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxau.mapper.ManageMapper;
import com.jxau.pojo.*;
import com.jxau.service.ManageService;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {

    @Resource
    private ManageMapper manageMapper;

    @Override
    public  ResultEntity<PageInfo<EssayPO>> selectAllEssays(HashMap<String, String> map) {
        try{
            int currentPage = Integer.parseInt(map.get("currentPage"));
            // 页面条数
            int pageSize = Integer.parseInt(map.get("pageSize"));
            // 参数一定要带state
            boolean state1 = map.containsKey("state");
            String state;
            if(state1) {
                state = map.get("state");
            }
            else{
                state = "";
            }
            PageHelper.startPage(currentPage,pageSize);
            List<EssayPO> essays = manageMapper.selectAllEssays(state);
            for (EssayPO e: essays) {
                e.setTag(e.getTags().split(","));
                // 根据文章id去查对应的图片集合
                ArrayList<String> imglist = manageMapper.selectEssayImagesById(e.getId());
                e.setImages(imglist);
            }
            PageInfo<EssayPO> essayPageInfo = new PageInfo<>(essays);
            return ResultEntity.successWithData(essayPageInfo);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity<String> audirEssays(HashMap<String, String> map) {
        try{
            String id = map.get("id");
            manageMapper.updateEssaysState(id);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> deleteEssays(HashMap<String, String> map) {
        try{
            String id = map.get("id");
            manageMapper.deleteEssays(id);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity<List<UserInfoVO>> getAllUsers(String state) {
        try{

            List<UserInfoVO> list = manageMapper.selectAllUser(state);

            return ResultEntity.successWithData(list);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> audirUsers(HashMap<String, String> map) {
        try{
            String userId = map.get("userId");
            manageMapper.updateUserState(userId);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity<String> deleteUser(HashMap<String, String> map) {
        try{
            String id = map.get("id");
            manageMapper.deleteUser(id);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
}
