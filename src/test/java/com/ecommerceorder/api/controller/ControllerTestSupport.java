package com.ecommerceorder.api.controller;


import com.ecommerceorder.api.controller.order.OrderController;
import com.ecommerceorder.api.controller.wishlist.WishlistController;
import com.ecommerceorder.api.service.order.OrderService;
import com.ecommerceorder.api.service.wishlist.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    WishlistController.class,
    OrderController.class,
})
@ActiveProfiles("test")
public abstract class ControllerTestSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected WishlistService wishlistService;

  @MockBean
  protected OrderService orderService;
}
