package com.ecommerceorder.global.feign.product.service;

import com.ecommerceorder.global.exception.CustomException;
import com.ecommerceorder.global.exception.ExceptionCode;
import com.ecommerceorder.global.feign.product.ProductFeignClient;
import com.ecommerceorder.global.feign.product.dto.UpdateQuantityByProductOptionsDto;
import com.ecommerceorder.global.feign.product.dto.UpdateQuantityByProductOptionsDto.ProductOptionInfo;
import feign.FeignException.ServiceUnavailable;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductFeignServiceImpl implements ProductFeignService {

  private final ProductFeignClient productFeignClient;

  @Override
  @Retry(name = "simpleRetryConfig")
  @CircuitBreaker(name = "simpleCircuitBreakerConfig", fallbackMethod = "fallback")
  public void updateProductStock(UpdateQuantityByProductOptionsDto dto) {
    productFeignClient.updateProductStock(dto);
  }

  private void fallback(UpdateQuantityByProductOptionsDto dto, ServiceUnavailable ex) {
    Collection<ProductOptionInfo> productOptionInfos = dto.productOptionInfos();
    for(ProductOptionInfo productOptionInfo : productOptionInfos) {
      log.info("fallback! your request ("
          + "productOptionId=" + productOptionInfo.getProductOptionId()
          + "&quantity="+ productOptionInfo.getQuantity() + ")");
    }
    throw CustomException.from(ExceptionCode.PRODUCT_SERVICE_UNAVAILABLE);
  }
}
