package com.ecommerceorder.global.feign.user.service;

import com.ecommerceorder.global.exception.CustomException;
import com.ecommerceorder.global.exception.ExceptionCode;
import com.ecommerceorder.global.feign.user.UserFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserFeignServiceImpl implements UserFeignService {

  private final UserFeignClient userFeignClient;

  @Override
  @Retry(name = "simpleRetryConfig")
  @CircuitBreaker(name = "simpleCircuitBreakerConfig", fallbackMethod = "fallback")
  public boolean existsMemberId(Long memberId) {
    return userFeignClient.existsMemberId(memberId);
  }

  private boolean fallback(Long memberId, Exception ex) {
    log.info("fallback! your request is " + memberId + ":" + ex.getMessage());
    throw CustomException.from(ExceptionCode.USER_SERVICE_UNAVAILABLE);
  }
}
