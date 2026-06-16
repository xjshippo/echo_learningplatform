package com.jxau.service.impl;

import cn.hutool.core.lang.UUID;
import com.jxau.mapper.FavoritesMapper;
import com.jxau.pojo.*;
import com.jxau.service.FavoritesService;
import com.jxau.util.ResultEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class FavoritesServiceImpl implements FavoritesService {

    @Resource
    private FavoritesMapper favoritesMapper;

    @Override
    public ResultEntity<String> saveInvitation(FavoritesVO favoritesVO) {
        try{
            favoritesMapper.insertInvitation(favoritesVO);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    /**
     * 创建收藏夹
     * @param name
     * @param currentUserId
     * @return
     */
    @Override
    public ResultEntity<String> createFavorites(String name, String currentUserId) {
        try{
            if(name == null) return ResultEntity.falseWithoutData("收藏夹名字为空");
            // 创建实体类，注入属性，保存到数据库中
            FavoritesPO favoritesPO = new FavoritesPO();
            favoritesPO.setId(UUID.randomUUID().toString().replace("-",""));
            favoritesPO.setCreateTime(new Date());
            favoritesPO.setUserId(currentUserId);
            favoritesPO.setName(name);

            favoritesMapper.saveFavorite(favoritesPO);

            return ResultEntity.successWithoutData();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultEntity<String> deleteFavorites(String favoritesId, String currentUserId) {
        try{
            FavoritesPO favoritesPO = new FavoritesPO();
            favoritesPO.setId(favoritesId);
            favoritesPO.setUserId(currentUserId);
            favoritesMapper.deleteFavoritesByFavoritesIdAndUserId(favoritesPO);
            favoritesMapper.deleteFavoritesWithInvitation(favoritesId,currentUserId);

            return ResultEntity.successWithoutData();
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<EssayUserVO>> getUserFavoritesEssay(String favoritesId, String currentUserId) {
        try{
            List<EssayUserVO> list = favoritesMapper.selectFavoritesEssay(favoritesId,currentUserId);
            for (EssayUserVO e: list) {
                e.setTag(e.getTags().split(","));
            }
            return ResultEntity.successWithData(list);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<FavoritesPOJO>> getUserAllFavorites(String currentUserId) {

        try{
            List<FavoritesPOJO> list = favoritesMapper.seleteUserAllFavorites(currentUserId);
            return ResultEntity.successWithData(list);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
}
