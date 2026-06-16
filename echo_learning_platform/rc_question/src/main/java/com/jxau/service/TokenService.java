package com.jxau.service;

import com.jxau.pojo.UserPO;
import io.jsonwebtoken.Claims;

public interface TokenService {

     String getToken (UserPO user);

     Claims getTokenClaim (String token);
}
