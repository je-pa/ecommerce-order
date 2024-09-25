/**
 * @Date : 2024. 09. 06.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.global.feign.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member")
public interface UserFeignClient {
  @GetMapping("/internal/v0/members/{memberId}/exists")
  boolean existsMemberId(@PathVariable Long memberId);

}