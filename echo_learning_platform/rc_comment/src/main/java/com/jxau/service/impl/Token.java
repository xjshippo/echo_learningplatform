package com.jxau.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jxau.pojo.UserPO;
import com.jxau.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    /**
     * 根据身份ID标识，生成Token
     */
    @Override
    public String getToken (UserPO user){
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000000);


        Map<String,Object> maps = Maps.newHashMap();
        maps.put("user",user);


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
    /**
     * 获取 Token 中注册信息
     */
    @Override
    public Claims getTokenClaim (String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
