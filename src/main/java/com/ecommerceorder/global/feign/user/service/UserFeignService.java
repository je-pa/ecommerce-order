package com.ecommerceorder.global.feign.user.service;

public interface UserFeignService {
  boolean existsMemberId(Long memberId);
}
