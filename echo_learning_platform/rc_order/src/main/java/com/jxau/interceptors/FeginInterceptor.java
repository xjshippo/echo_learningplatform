package com.jxau.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeginInterceptor implements RequestInterceptor {
    public FeginInterceptor() {
    }

    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> headers = this.getHeaders();
        Iterator var3 = headers.keySet().iterator();

        while(var3.hasNext()) {
            String headerName = (String)var3.next();
            requestTemplate.header(headerName, new String[]{(String)this.getHeaders().get(headerName)});
        }

    }

    private Map<String, String> getHeaders() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = new LinkedHashMap();
        Enumeration enumeration = request.getHeaderNames();

        while(enumeration.hasMoreElements()) {
            String key = (String)enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        // 设置请求头
        map.put("currentUser",(String) request.getAttribute("currentUser"));
        return map;
    }
}
