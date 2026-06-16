package com.jxau.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 视频工具类
 * @author chenzouquan
 */
public class VideoUtil {

    public static final String ACCESS_KEY_ID = "your-access-key-id";
    public static final String ACCESS_KEY_SECRET ="your-access-key-secret";

    public static DefaultAcsClient initVodClient() throws ClientException {
        // 点播服务接入地域
        String regionId = "cn-shanghai";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 上传视频
     * @param file
     * @return
     */
    public static String uploadVideoAly(MultipartFile file) {

//        try {
//            //accessKeyId, accessKeySecret
//            //fileName：上传文件原始名称
//            String fileName = file.getOriginalFilename();
//            //title：上传之后显示名称
//            String title = fileName.substring(0, fileName.lastIndexOf("."));
//            //inputStream：上传文件输入流
//            InputStream inputStream = file.getInputStream();
//            UploadStreamRequest request = new UploadStreamRequest(ACCESS_KEY_ID,ACCESS_KEY_SECRET, title, fileName, inputStream);
//            UploadVideoImpl uploader = new UploadVideoImpl();
//            UploadStreamResponse response = uploader.uploadStream(request);
//
//            String videoId = null;
//            if (response.isSuccess()) {
//                videoId = response.getVideoId();
//            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
//                videoId = response.getVideoId();
//            }
//            return videoId;
//        }catch(Exception e) {
//            e.printStackTrace();
//            return null;
//        }

        return null;

    }

    /**
     * 获取播放地址函数
     * @param client
     * @param videoId
     * @return
     * @throws Exception
     */
    public static String getPlayInfo(DefaultAcsClient client,String videoId) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoId);
        GetPlayInfoResponse acsResponse = client.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = acsResponse.getPlayInfoList();
        return playInfoList.get(0).getPlayURL();
    }

    /*以下为调用示例
    public static void main(String[] argv) throws ClientException {
        DefaultAcsClient client = initVodClient();
        String videoAly = uploadVideoAly(file);
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            response = getPlayInfo(client,videoAly);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
    */
}
