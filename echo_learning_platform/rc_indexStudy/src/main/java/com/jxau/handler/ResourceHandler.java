package com.jxau.handler;

import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;
import com.jxau.pojo.*;
import com.jxau.service.CommentService;
import com.jxau.service.ResourceService;
import com.jxau.util.ResultEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author chenzouquan
 */
@Slf4j
@Api(tags = "资源")
@RestController
@RequestMapping("/index")
public class ResourceHandler {
    @Autowired
    ResourceService resourceService;

    /*
    @ApiOperation(value = "资源管理-添加资源",notes = "用户上传资源")
    @PostMapping("/add/one/resource")
    @Authorize
    public ResultEntity<String> addOneResource(@RequestParam String content,@RequestParam String type,@RequestParam String tags, HttpServletRequest request, @RequestParam MultipartFile[] files){
        HashMap map = new HashMap();
        map.put("content",content);
        map.put("type",type);
        map.put("tags",tags);
        String userId = (String) request.getAttribute("currentUser");
        map.put("userId",userId);
        String endMessage = resourceService.addOneResource(map,files);
        if (!ResultEntity.SUCCESS.equals(endMessage)){
            return ResultEntity.falseWithoutData(endMessage);
        }
        return ResultEntity.successWithoutData();
    }*/

    @ApiOperation(value = "资源管理-查看资源",notes = "用户查看资源")
    @PostMapping("get/all/resource")
    public ResultEntity<HashMap<String,Object>> getAllResource(@RequestBody HashMap<String,String> map){
        PageInfo<ResourcePO> pageInfo = resourceService.selectAllResource(map);
        HashMap<String,Object> result = new HashMap<>();
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查询失败");
        }
        ArrayList<ResourceVO> resourceVOS = new ArrayList<>();
        for (ResourcePO tmp: pageInfo.getList()) {
            resourceVOS.add(new ResourceVO(
                tmp.getId(),tmp.getContent(),tmp.getType(), tmp.getSize(),tmp.getUrl()
            ));
        }
        result.put("list",resourceVOS);
        result.put("total",pageInfo.getTotal());
        result.put("pages",pageInfo.getPages());
        result.put("pageNum",pageInfo.getPageNum());
        result.put("pageSize",pageInfo.getPageSize());
        return ResultEntity.successWithData(result);
    }

    @ApiOperation(value = "资源管理-查看视频",notes = "用户查看视频")
    @PostMapping("get/all/video")
    public ResultEntity<HashMap<String,Object>> getAllVideo(@RequestBody HashMap<String,String> map){
        PageInfo<VideoPO> pageInfo = resourceService.selectAllVideo(map);
        HashMap<String,Object> result = new HashMap<>();
        if (pageInfo == null){
            return ResultEntity.falseWithoutData("查询失败");
        }
        ArrayList<VideoVO> videoVOS = new ArrayList<>();

        for (VideoPO tmp: pageInfo.getList()) {
            String[] avatarList = new String[0];
            if (tmp.getLookUserPos() != null) {
                avatarList = new String[tmp.getLookUserPos().size()];
                int i = 0;
                for (LookUserPo t: tmp.getLookUserPos()) {
                    if (t.getUser() != null) {
                        avatarList[i++] = t.getUser().getImageUrl();
                    }
                }
            }
            String userImg = (tmp.getUser() != null) ? tmp.getUser().getImageUrl() : "";
            String userName = (tmp.getUser() != null) ? tmp.getUser().getNickName() : "";
            videoVOS.add(new VideoVO(
                    tmp.getId(),
                    userImg,
                    userName,
                    tmp.getContent(),
                    tmp.getCategory(),
                    avatarList,
                    tmp.getImageUrl(),
                    tmp.getUrl(),
                    tmp.getCreate_time()
            ));
        }
        result.put("list",videoVOS);
        result.put("total",pageInfo.getTotal());
        result.put("pages",pageInfo.getPages());
        result.put("pageNum",pageInfo.getPageNum());
        result.put("pageSize",pageInfo.getPageSize());
        return ResultEntity.successWithData(result);
    }

    @ApiOperation(value = "资源管理-用户观看视频",notes = "用户观看视频")
    @PostMapping("look/one/video")
    public ResultEntity<String> lookOneVideo(@RequestBody HashMap<String,String> map){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = (String) request.getAttribute("currentUser");
        map.put("userId",userId);
        String resultEntity = resourceService.addOneLook(map);
        if (!ResultEntity.SUCCESS.equals(resultEntity)){
            return ResultEntity.falseWithoutData(resultEntity);
        }
        return ResultEntity.successWithoutData();
    }

    @ApiOperation(value = "资源管理-删除资源",notes = "用户删除一篇或者多个资源")
    @PostMapping("/delete/resource")
    public ResultEntity<String> deleteResource(@RequestBody HashMap<String,Object> map){
        String endMessage = resourceService.deleteResource(map);
        if (!ResultEntity.SUCCESS.equals(endMessage)){
            return ResultEntity.falseWithoutData(endMessage);
        }
        return ResultEntity.successWithoutData();
    }

    /*
        @ApiOperation(value = "资源管理-点赞资源",notes = "用户点赞一篇或者多篇资源")
        @PostMapping("/like/one/resource")
        public ResultEntity<String> likeOneResource(@RequestBody HashMap<String,String> map){
            String endMessage = resourceService.likeOneResource(map);
            if (!ResultEntity.SUCCESS.equals(endMessage)){
                return ResultEntity.falseWithoutData(endMessage);
            }
            return ResultEntity.successWithoutData();

        }
    */
}
