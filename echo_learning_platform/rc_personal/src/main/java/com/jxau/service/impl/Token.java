package com.jxau.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jxau.exception.TokenExpireException;
import com.jxau.pojo.UserPO;
import com.jxau.service.TokenService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class Token implements TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private long expire;

    @Value("${jwt.header}")
    private String header;


    /*
     * 根据身份ID标识，生成Token
     */
    public String getToken (UserPO user){
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000000);

        //Date expireDate = new Date(nowDate.getTime() + expire * 1000);// 1小时过期（生产环境）

       // Date expireDate = new Date(nowDate.getTime() + 10);// 测试

        Map<String,Object> maps = Maps.newHashMap();
        maps.put("user",user);
        //maps.put("sessionKey",session_key);

        return Jwts.builder()
                // header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg","HS256")
                // payload
                .setSubject(JSONObject.toJSONString(maps))
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .setId(UUID.randomUUID().toString())
                // signature
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();


    }
    /*
     * 获取 Token 中注册信息
     */
    public Claims getTokenClaim (String token) {

            JwtParser jwtParser = Jwts.parser().setSigningKey(secret);
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            return body;
    }

}
