package com.jxau.service;

import com.jxau.pojo.*;
import com.jxau.util.ResultEntity;

import java.util.List;

public interface FavoritesService {

    ResultEntity<String> createFavorites(String name, String currentUserId);

    ResultEntity<String> deleteFavorites(String favoritesId, String currentUserId);

    ResultEntity<String> saveInvitation(FavoritesVO favoritesVO);

    ResultEntity<List<EssayUserVO>> getUserFavoritesEssay(String favoritesId, String currentUserId);

    ResultEntity<List<FavoritesPOJO>> getUserAllFavorites(String currentUserId);
}
