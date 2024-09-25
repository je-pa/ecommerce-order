/**
 * @Date : 2024. 09. 06.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.global.feign.product;

import com.ecommerceorder.global.feign.product.dto.UpdateQuantityByProductOptionsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product")
public interface ProductFeignClient {
  @PutMapping("/internal/v0/products/options/stock")
  void updateProductStock(@RequestBody UpdateQuantityByProductOptionsDto dto);
}
