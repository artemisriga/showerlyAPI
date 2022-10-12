package com.showerly.api.config.interceptor;

import com.showerly.api.service.PriceCalculatorService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PriceUpdateInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PriceCalculatorService.updatePrice();
        return true;
    }
}
