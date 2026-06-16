package com.jxau.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.jxau.annotations.Authorize;
import com.jxau.pojo.UserPO;
import com.jxau.service.UserService;
import com.jxau.service.impl.Token;
import com.jxau.util.ResultEntity;
import io.jsonwebtoken.Claims;
import java.lang.reflect.Method;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Resource
    private Token tokenObj;
    @Resource
    private UserService userService;

    public AuthorizationInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (!(object instanceof HandlerMethod)) {
            return true;
        } else {
            HandlerMethod handlerMethod = (HandlerMethod)object;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(Authorize.class)) {
                WebApplicationContext factory;
                if (this.tokenObj == null) {
                    System.out.println("tokenObj is null!!!");
                    factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    this.tokenObj = (Token)factory.getBean("token");
                }

                if (this.userService == null) {
                    System.out.println("userService is null!!!");
                    factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    this.userService = (UserService)factory.getBean("userServiceImpl");
                }

                Authorize userLoginToken = (Authorize)method.getAnnotation(Authorize.class);
                if (userLoginToken.required()) {
                    String token = request.getHeader("token");
                    if (token == null) {
                        throw new RuntimeException("无效的Token，请用户登录！");
                    }

                    try {
                        Claims tokenClaim = this.tokenObj.getTokenClaim(token);
                        String subject = tokenClaim.getSubject();
                        Map obj = (Map)JSONObject.parseObject(subject, Map.class);
                        UserPO user = (UserPO)JSONObject.parseObject(obj.get("user").toString(), UserPO.class);
                        ResultEntity<UserPO> result = this.userService.getUserByUserId(user.getId());
                        if ("FALSE".equals(result.getResult()) || result.getData() == null) {
                            throw new RuntimeException("用户不存在，请重新登录!");
                        }

                        UserPO data = (UserPO)result.getData();
                        if (!user.getPassWord().equals(data.getPassWord())) {
                            throw new RuntimeException("密码错误！");
                        }

                        request.setAttribute("currentUser", user.getId());
                        request.setAttribute("user", data);
                    } catch (Exception var14) {
                        var14.printStackTrace();
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
