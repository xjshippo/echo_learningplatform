package com.jxau.service;

import com.jxau.pojo.EssayUserVO;
import com.jxau.pojo.FavoritesPOJO;
import com.jxau.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "rc-personal-service",contextId = "FavoritesService")
@Service
public interface FavoritesService {
    @PostMapping(value = "/user/save/collection", consumes = "application/json")
    public ResultEntity<String> userSaveCollection(@RequestBody Map map);

    @PostMapping(value = "/user/save/favorites", consumes = "application/json")
    public ResultEntity<String> userCreateFavorites(@RequestBody Map map);

    /**
     * 可能存在问题,需要改变
     * @param map
     * @return
     */
    @DeleteMapping(value = "/user/delete/favorites", consumes = "application/json")
    public ResultEntity<String> userdeleteFavorites(@RequestBody Map map);

    @PostMapping(value = "/user/get/favorites/essay", consumes = "application/json")
    public ResultEntity<List<EssayUserVO>> userGetFavoritesEssay(@RequestBody Map map);

    @GetMapping("/user/get/all/favorites")
    public ResultEntity<List<FavoritesPOJO>> userGetAllFavorites();
}
