/**
 * @Date : 2024. 08. 27.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.api.controller.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerceorder.api.controller.ControllerTestSupport;
import com.ecommerceorder.api.controller.order.dto.request.CreateOrderByWishlistItemsRequest;
import com.ecommerceorder.api.controller.order.dto.request.OrderStatusRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderControllerTest extends ControllerTestSupport {

  @DisplayName("주문을 진행한다.")
  @Test
  void sendAuthCode() throws Exception {
    // given
    List<CreateOrderByWishlistItemsRequest> requests =
        List.of(new CreateOrderByWishlistItemsRequest(1L));

    mockMvc.perform(
            post("/api/v0/orders")
                .content(objectMapper.writeValueAsString(requests))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isOk());
  }

  @DisplayName("주문 상태를 업데이트 한다.")
  @Test
  void setStatus() throws Exception {
    mockMvc.perform(
            patch("/api/v0/orders/1/status")
                .content(objectMapper.writeValueAsString(OrderStatusRequest.CANCEL))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isOk());
  }
}