package com.jxau.controller;

import com.jxau.annotations.Authorize;
import com.jxau.annotations.SystemControllerLog;
import com.jxau.pojo.*;
import com.jxau.service.FavoritesService;
import com.jxau.util.ResultEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FavoritesController {


    @Resource
    private FavoritesService favoritesService;

    /**
     * 用户添加收藏
     * @param map
     * @return
     */
    @PostMapping("/user/save/collection")
    public ResultEntity<String> userSaveCollection(@RequestBody HashMap map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String invitationId = (String)map.get("invitationId");// 文章的id
        String favoritesId = (String)map.get("favoritesId");// 收藏夹的id
        String currentUserId =(String) request.getHeader("currentUser");
        FavoritesVO favoritesVO = new FavoritesVO(favoritesId, currentUserId, invitationId,new Date());
        return favoritesService.saveInvitation(favoritesVO);

    }


    /**
     * 用户创建收藏夹
     * @param map
     * @return
     */
    //@SystemControllerLog(descrption = "用户操作", actionType = "用户创建收藏夹")
    @PostMapping("/user/save/favorites")
    public ResultEntity<String> userCreateFavorites(@RequestBody Map map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String name = (String)map.get("collectName");
        String currentUserId =(String) request.getHeader("currentUser");
        return favoritesService.createFavorites(name, currentUserId);

    }

    /**
     * 用户删除文件夹
     * @param map
     * @return
     */
    @SystemControllerLog(descrption = "用户操作", actionType = "用户删除收藏夹")
    @DeleteMapping("/user/delete/favorites")
    public ResultEntity<String> userdeleteFavorites(@RequestBody Map map){

        /**
         * 删除文件夹后，还得把文件夹中的所有东西删除
         */
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String favoritesId = (String)map.get("favoritesId");
        String currentUserId =(String) request.getHeader("currentUser");
        return favoritesService.deleteFavorites(favoritesId, currentUserId);

    }

/*
    */
/**
     * 用户从收藏夹中获取收藏文章
     *//*

    @PostMapping("/user/get/favorites/essay")
    @Authorize
    public ResultEntity<List<EssayPOVO>> userGetFavoritesEssay(@RequestBody Map map, HttpServletRequest request){

        // 根据用户id从用户收藏夹中获取收藏夹中的文章
        String favoritesId = (String)map.get("favoritesId");
        String currentUserId =(String) request.getHeader("currentUser");
        return favoritesService.getUserFavoritesEssay(favoritesId, currentUserId);
    }
*/

    /**
     * 用户从收藏夹中获取收藏文章
     */
    @PostMapping("/user/get/favorites/essay")
    public ResultEntity<List<EssayUserVO>> userGetFavoritesEssay(@RequestBody Map map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 根据用户id从用户收藏夹中获取收藏夹中的文章
        String favoritesId = (String)map.get("favoritesId");
        String currentUserId =(String) request.getHeader("currentUser");
        return favoritesService.getUserFavoritesEssay(favoritesId, currentUserId);
    }

    /**
     * 获取用户的收藏夹列表
     * @return
     */
    @GetMapping("/user/get/all/favorites")
    public ResultEntity<List<FavoritesPOJO>> userGetAllFavorites(){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId =(String) request.getHeader("currentUser");
        return favoritesService.getUserAllFavorites(currentUserId);
    }

}
