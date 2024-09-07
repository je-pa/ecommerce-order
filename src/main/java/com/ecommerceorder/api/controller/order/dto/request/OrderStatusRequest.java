/**
 * @Date : 2024. 08. 28.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.api.controller.order.dto.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatusRequest {
  CANCEL("취소"),
  REQUESTED_RETURN("반품신청된"),;

  private final String name;

  public String getName() {
    return name;
  }
}