package com.jxau.aspect;



import com.jxau.annotations.SystemControllerLog;
import com.jxau.pojo.LogPO;
import com.jxau.pojo.UserPO;
import com.jxau.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.UUID;

@Aspect
@Component
public class SystemLogAspect {

    @Autowired
    private LogService ls;

    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);


    //Controller层切点
    @Pointcut("execution (* com.jxau.controller..*.*(..))")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        /*用isDebugEnabled方法判断下是能提升性能的*/
        if (logger.isInfoEnabled()) {
            logger.info("before " + joinPoint);
        }
    }
    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public void after(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 执行顺序：先进入过滤器、在进入拦截器，最后进入切面
        // 从请求域中获取user

        UserPO user =(UserPO) request.getAttribute("user");

        //请求的IP
        String ip = request.getRemoteAddr();

        if (user != null) {

        System.out.println("userid会打印什么 : " + user.getId());
        try {
            //获取HTTP响应状态码
           /* String requestURI = request.getRequestURI();// 获取请求路径
            URL u = new URL(requestURI);
            URLConnection uc = u.openConnection();// 建立URL连接
            HttpURLConnection huc = (HttpURLConnection)uc;
            String code = new Integer(huc.getResponseCode()).toString();// 从连接中建立状态响应码*/

            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);// 通过反射得到在切点中的类的字节码信息
            Method[] methods = targetClass.getMethods();
            String logType = "";
            String logContent = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        logType = method.getAnnotation(SystemControllerLog.class).actionType();// 通过反射得到目标类上面注解的相关信息
                        logContent = method.getAnnotation(SystemControllerLog.class).descrption();
                        break;
                    }
                }
            }
            //*========控制台输出=========*//
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + logContent);
            System.out.println("请求人:" + user.getNickName());
            //*========数据库日志=========*//
            LogPO log = new LogPO();
            log.setId(UUID.randomUUID().toString().replace("-",""));
            log.setUserId(user.getId());
            log.setType(logType);
            log.setUserName(user.getNickName());
            log.setContent(logContent);
            //log.setLogErrorCode(HttpStatus.OK.toString()); 设置响应状态码
            log.setCode(HttpStatus.OK.toString());
            log.setCreateTime(new Date());
            log.setIp(ip);
            //保存数据库
            ls.logSave(log);

        } catch (Exception e) {
            //记录本地异常日志
            logger.error("异常信息:{}", e.getMessage());
        }

    }
    }

    //配置后置返回通知,使用在方法aspect()上注册的切入点
    @AfterReturning("controllerAspect()")
    public void afterReturn(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) {
            logger.info("afterReturn " + joinPoint);
        }
    }

    /**
     * 异常通知 用于拦截记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 执行顺序：先进入过滤器、在进入拦截器，最后进入切面
        // 从请求域中获取user
        UserPO user =(UserPO) request.getAttribute("user");

        //请求的IP
        String ip = request.getRemoteAddr();

         if(user!=null){

        try {
            //获取HTTP响应状态码
            /*String requestURI = request.getRequestURI();// 获取请求路径
            URL u = new URL(requestURI);
            URLConnection uc = u.openConnection();// 建立URL连接
            HttpURLConnection huc = (HttpURLConnection)uc;
            String code = new Integer(huc.getResponseCode()).toString();// 从连接中建立状态响应码*/


            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String logType = "";
            String logContent = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        logType = method.getAnnotation(SystemControllerLog.class).actionType();
                        logContent = method.getAnnotation(SystemControllerLog.class).descrption();
                        break;
                    }
                }
            }
            /*========控制台输出=========*/
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + logContent);
            System.out.println("请求人:" + user.getNickName());
            /*==========数据库日志=========*/
            LogPO log = new LogPO();
            log.setUserId(UUID.randomUUID().toString().replace("-",""));
            log.setUserId(user.getId());
            log.setUserName(user.getNickName());
            log.setType(logType);
            log.setContent(logContent);
            //log.setLogErrorCode(HttpStatus.OK.toString()); 设置响应状态码
            log.setCode(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.toString());
            log.setCreateTime(new Date());
            log.setIp(ip);
            //保存数据库
            ls.logSave(log);
        } catch (Exception ex) {
            //记录本地异常日志
            logger.error("异常信息:{}", ex.getMessage());
        }
        /*==========记录本地异常日志==========*/
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage());


         }


    }

}
