package com.example.eureka.client.order.global.login;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class)  && parameter.getParameterType().equals(
            CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Long currentUserId = Long.parseLong(webRequest.getHeader("X-User-Id"));
        String currentUserRole = webRequest.getHeader("X-User-Role");

        CurrentUser currentUser = new CurrentUser(currentUserId, currentUserRole);
        return currentUser;
    }
}
