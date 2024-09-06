package com.ecommerceorder.api.controller.wishlist;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerceorder.api.controller.ControllerTestSupport;
import com.ecommerceorder.api.controller.wishlist.dto.request.Action;
import com.ecommerceorder.api.controller.wishlist.dto.request.CreateWishlistRequest;
import com.ecommerceorder.api.controller.wishlist.dto.request.UpdateWishlistItemQuantityRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class WishlistControllerTest extends ControllerTestSupport {

  @DisplayName("상품 옵션을 선택하고 위시리스트에 등록한다.")
  @Test
  void set() throws Exception{
    // given
    List<CreateWishlistRequest> requests = List.of(
      new CreateWishlistRequest(1L, 1),
      new CreateWishlistRequest(2L, 1),
      new CreateWishlistRequest(3L, 1)
    );

    mockMvc.perform(
            post("/api/v0/wishlists")
                .content(objectMapper.writeValueAsString(requests))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isOk());
  }

  @DisplayName("위시리스트를 조회한다.")
  @Test
  void getList() throws Exception {
    // given
    Long memberId = 123L;

    // when
    // then
    mockMvc.perform(
            get("/api/v0/wishlists")
                .queryParam("memberId", memberId.toString())
        ).andDo(print())
        .andExpect(status().isOk());

  }

  @DisplayName("위시리스트 항목을 수정한다.")
  @Test
  void updateItemQuantity() throws Exception {
    // given
    Long itemId = 1L;
    UpdateWishlistItemQuantityRequest request
        = new UpdateWishlistItemQuantityRequest(Action.DECREASE);
    // when
    mockMvc.perform(
            patch("/api/v0/wishlists/items/"+itemId+"/quantity")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isOk());

  }
}