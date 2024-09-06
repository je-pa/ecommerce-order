package com.ecommerceorder.global.resolver;

import com.ecommerceorder.api.controller.annotation.CurrentMemberId;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentMemberIdResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentMemberId.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
    String memberId = request.getHeader("X-Member-Id");
    if (memberId != null) {
      return Long.valueOf(memberId);
    }
    return null;
  }

}
