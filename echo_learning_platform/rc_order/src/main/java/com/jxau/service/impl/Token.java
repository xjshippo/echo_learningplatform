//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jxau.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jxau.pojo.UserPO;
import com.jxau.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Token implements TokenService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire}")
    private long expire;
    @Value("${jwt.header}")
    private String header;

    public Token() {
    }

    public String getToken(UserPO user) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + this.expire * 1000000L);
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("user", user);
        return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("alg", "HS256").setSubject(JSONObject.toJSONString(maps)).setIssuedAt(nowDate).setExpiration(expireDate).setId(UUID.randomUUID().toString()).signWith(SignatureAlgorithm.HS512, this.secret).compact();
    }

    public Claims getTokenClaim(String token) {
        try {
            return (Claims)Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
