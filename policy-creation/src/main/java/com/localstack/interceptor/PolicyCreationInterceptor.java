package com.localstack.interceptor;

import com.localstack.constants.Constants;
import com.localstack.exception.PolicyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class PolicyCreationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String x_api_header = request.getHeader(Constants.X_API_HEADER);
        if (!(x_api_header != null && x_api_header.length() > 10))
            throw new PolicyException("UnAuthorized");
        return true;
    }
}
