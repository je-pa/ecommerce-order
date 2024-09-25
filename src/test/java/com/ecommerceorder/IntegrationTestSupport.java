package com.ecommerceorder;

import com.ecommerceorder.global.feign.product.ProductFeignClient;
import com.ecommerceorder.global.feign.user.service.UserFeignService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

  @MockBean
  protected UserFeignService userFeignService;

  @MockBean
  protected ProductFeignClient productFeignClient;
}
