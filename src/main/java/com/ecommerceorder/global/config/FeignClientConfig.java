/**
 * @Date : 2024. 09. 06.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.ecommerceorder.global.feign")
@Configuration
public class FeignClientConfig {

}
