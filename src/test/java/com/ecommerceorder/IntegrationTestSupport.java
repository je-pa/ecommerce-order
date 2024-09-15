package com.ecommerceorder;

import com.ecommerceorder.global.feign.ProductFeignClient;
import com.ecommerceorder.global.feign.UserFeignClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

  @MockBean
  protected UserFeignClient userFeignClient;

  @MockBean
  protected ProductFeignClient productFeignClient;
}
