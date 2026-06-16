package com.jxau.mapper;

import com.jxau.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoritesMapper {

    void saveFavorite(FavoritesPO favoritesPO);

    void deleteFavoritesByFavoritesIdAndUserId(FavoritesPO favoritesPO);

    void insertInvitation(FavoritesVO favoritesVO);

    void deleteFavoritesWithInvitation(@Param("favoritesId") String favoritesId,@Param("currentUserId") String currentUserId);

    List<EssayUserVO> selectFavoritesEssay(@Param("favoritesId")String favoritesId, @Param("currentUserId") String currentUserId);

    List<FavoritesPOJO> seleteUserAllFavorites(@Param("currentUserId")String currentUserId);
}
