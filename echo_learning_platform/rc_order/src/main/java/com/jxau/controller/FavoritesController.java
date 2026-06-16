package com.jxau.controller;

import com.jxau.annotations.Authorize;
import com.jxau.pojo.EssayUserVO;
import com.jxau.pojo.FavoritesPOJO;

import com.jxau.service.FavoritesService;
import com.jxau.service.UserService;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    /*@Autowired
    private UserService userService;*/

    @PostMapping("/user/save/collection")
    @Authorize
    public ResultEntity<String> userSaveCollection(@RequestBody HashMap map){
        try{
            return favoritesService.userSaveCollection(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/user/save/favorites")
    @Authorize
    public ResultEntity<String> userCreateFavorites(@RequestBody HashMap map){
        try{
            return favoritesService.userCreateFavorites(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @DeleteMapping("/user/delete/favorites")
    @Authorize
    public ResultEntity<String> userdeleteFavorites(@RequestBody Map map){
        try{
            return favoritesService.userdeleteFavorites(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/user/get/favorites/essay")
    @Authorize
    public ResultEntity<List<EssayUserVO>> userGetFavoritesEssay(@RequestBody Map map){
        try{
            return favoritesService.userGetFavoritesEssay(map);
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/user/get/all/favorites")
    @Authorize
    public ResultEntity<List<FavoritesPOJO>> userGetAllFavorites(){
        try{
            return favoritesService.userGetAllFavorites();
        }catch (Exception e) {
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
}
