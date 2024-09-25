package com.ecommerceorder.global.feign.product.service;

import com.ecommerceorder.global.feign.product.dto.UpdateQuantityByProductOptionsDto;

public interface ProductFeignService {
  void updateProductStock(UpdateQuantityByProductOptionsDto dto);
}
