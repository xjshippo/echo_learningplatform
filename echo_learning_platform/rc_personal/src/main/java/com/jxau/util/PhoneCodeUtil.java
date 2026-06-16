package com.jxau.util;

import cn.hutool.crypto.digest.MD5;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class PhoneCodeUtil {

    public static ResultEntity<String> SendCode(String phone)
    {
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "a8686627cb1e42ac9b97b800568b54ac";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();

        // 随机生成6位验证码
        MD5 md5 = MD5.create();
        String s = md5.digestHex(phone);
        String substring = s.substring(26, s.length());
        String code = "code:"+substring;

        bodys.put("content", code+",expire_at:5");
        bodys.put("phone_number", phone);
        bodys.put("template_id", "TPL_0001");// 【深智科技】您的验证码是:%{code}，有效期%{expire_at}分钟。

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            // 成功就返回发送的验证码
            return ResultEntity.successWithData(substring);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }
}
