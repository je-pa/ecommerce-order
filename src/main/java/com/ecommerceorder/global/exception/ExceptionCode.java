package com.ecommerceorder.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

  // BAD_REQUEST:400:잘못된요청
  WISHLIST_OPTIONS_SAME_PRODUCT_ONLY(BAD_REQUEST, "모든 옵션의 상품이 같아야 합니다."),
  REQUESTS_EMPTY(BAD_REQUEST, "필수 요청 파라미터가 비었습니다."),
  PRODUCT_OPTION_DUPLICATE(BAD_REQUEST, "중복된 상품 옵션이 있습니다."),
  WISHLIST_ITEM_DUPLICATE(BAD_REQUEST, "중복된 상품 옵션이 있습니다."),
  ORDER_CANCEL_INVALID_STATE(BAD_REQUEST, "취소가 가능한 상태가 아닙니다."),
  ORDER_RETURN_INVALID_STATE(BAD_REQUEST, "반품이 가능한 상태가 아닙니다."),

  // FORBIDDEN:403:권한이슈
  UNAUTHORIZED_ACCESS(FORBIDDEN, "해당 정보를 조회할 수 있는 권한이 없습니다"),

  // NOT_FOUND:404:자원없음
  USER_NOT_FOUND(NOT_FOUND, "유저 개체를 찾지 못했습니다."),
  PRODUCT_OPTIONS_NOT_FOUND(NOT_FOUND, "없는 상품 옵션이 있습니다."),
  PRODUCT_OPTION_NOT_FOUND(NOT_FOUND, "상품 옵션 개체를 찾지 못했습니다."),
  WISHLIST_ITEM_NOT_FOUND(NOT_FOUND, "위시리스트 항목 개체를 찾지 못했습니다."),
  ORDER_NOT_FOUND(NOT_FOUND, "주문 개체를 찾지 못했습니다."),


  USER_SERVICE_UNAVAILABLE(SERVICE_UNAVAILABLE, "유저 서비스 사용에 실패했습니다."),
  PRODUCT_SERVICE_UNAVAILABLE(SERVICE_UNAVAILABLE, "제품 서비스 사용에 실패했습니다."),

  ;
  private final HttpStatus status;
  private final String message;

}