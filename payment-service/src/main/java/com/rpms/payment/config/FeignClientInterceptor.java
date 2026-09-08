package com.rpms.payment.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 1. Get the current HTTP request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            
            // 2. Extract the Authorization header (JWT token)
            String authorizationHeader = request.getHeader("Authorization");
            
            // 3. Forward it to the downstream service (Booking Service)
            if (authorizationHeader != null) {
                template.header("Authorization", authorizationHeader);
            }
        }
    }
}