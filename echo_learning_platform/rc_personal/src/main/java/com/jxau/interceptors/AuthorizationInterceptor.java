package com.jxau.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.jxau.annotations.Authorize;
import com.jxau.exception.TokenErrorException;
import com.jxau.exception.TokenExpireException;
import com.jxau.exception.TokenIsNoneException;
import com.jxau.pojo.UserPO;
import com.jxau.service.UserService;
import com.jxau.service.impl.Token;
import com.jxau.service.impl.UserServiceImpl;
import com.jxau.util.ResultEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 拦截器：验证Token的合法性
 */
public class AuthorizationInterceptor implements HandlerInterceptor {


    @Resource
    private Token tokenObj ;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {




        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        //检查有没有需要用户权限的注解
        //如果有注解Authorize，就需要验证token
        if (method.isAnnotationPresent(Authorize.class)) {

            if (tokenObj == null) {//解决service为null无法注入问题
                System.out.println("tokenObj is null!!!");
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                tokenObj = (Token) factory.getBean("token");
            }

            if (userService == null) {//解决service为null无法注入问题
                System.out.println("userService is null!!!");
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                userService = (UserService) factory.getBean("userServiceImpl");
            }



            Authorize userLoginToken = method.getAnnotation(Authorize.class);
            if (userLoginToken.required()) {
                // 开启验证
                String token = request.getHeader("authorization");
                if(token == null)
                {
                    throw new TokenIsNoneException(511,"身份信息缺失，登录异常");
                }
                Claims tokenClaim = null;
                try{
                    // 获取Token中的userId,然后跟数据库中的数据比对
                     tokenClaim = tokenObj.getTokenClaim(token);

                    //如果token过期了
                    String subject = tokenClaim.getSubject();
                    Map obj = JSONObject.parseObject(subject,Map.class);
                    //UserPO user =  JSONObject.parseObject(obj.get("user"), UserPO.class);
                    // token中携带的user
                    UserPO user = JSONObject.parseObject(obj.get("user").toString(), UserPO.class);
                    //obj.get("user").toString();
                    // 数据库中的user
                    ResultEntity<UserPO> result = userService.getUserByUserId(user.getId());
                    if(ResultEntity.FALSE.equals(result.getResult()) || result.getData() == null)
                    {
                        throw new RuntimeException("用户不存在!");
                    }
                    // 判断密码或者权限
                    UserPO data = result.getData();
                    if(!user.getPassWord().equals(data.getPassWord())){
                        // 验证失败
                        throw new RuntimeException("密码错误！");
                    }
                    // 添加request参数，用于传递userid
                    request.setAttribute("currentUser", user.getId());
                    request.setAttribute("user",data);
                }catch (Exception e)
                {
                    if(e instanceof io.jsonwebtoken.ExpiredJwtException){
                       throw new TokenExpireException(511,"身份信息已过期，请重新登录！");
                    }

                    if(e instanceof io.jsonwebtoken.SignatureException){
                        throw new TokenErrorException(511,"身份信息异常，登录失败！");
                    }

                    e.printStackTrace();
                    return false;
                }
            }

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
