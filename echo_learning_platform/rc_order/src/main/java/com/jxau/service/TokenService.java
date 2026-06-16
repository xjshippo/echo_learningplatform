package com.jxau.service;

import com.jxau.pojo.UserPO;
import io.jsonwebtoken.Claims;

public interface TokenService {
    String getToken(UserPO var1);

    Claims getTokenClaim(String var1);
}
