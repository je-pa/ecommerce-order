/**
 * @Date : 2024. 08. 28.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.domain.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.ecommerceorder.IntegrationTestSupport;
import com.ecommerceorder.domain.order.entity.Order;
import com.ecommerceorder.domain.order.entity.OrderItem;
import com.ecommerceorder.domain.order.type.OrderStatus;
import com.ecommerceorder.domain.product.entity.Product;
import com.ecommerceorder.domain.product.entity.ProductOption;
import com.ecommerceorder.domain.product.repository.ProductOptionRepository;
import com.ecommerceorder.domain.product.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class OrderItemRepositoryTest extends IntegrationTestSupport {
  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductOptionRepository productOptionRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @DisplayName("orderId로 OrderItem을 조회할 수 있다.")
  @Test
  void findAllByOrder(){
    // given
    Product product1 = productRepository.save(createProduct("상품1"));
    Product product2 = productRepository.save(createProduct("상품2"));

    ProductOption option1 = productOptionRepository.save(createProductOption("옵션1-1", product1));
    ProductOption option2 = productOptionRepository.save(createProductOption("옵션2-2", product2));
    ProductOption option3 = productOptionRepository.save(createProductOption("옵션2-3", product2));
    ProductOption option4 = productOptionRepository.save(createProductOption("옵션2-4", product2));

    Order order1 = orderRepository.save(
        createOrder(1L, OrderStatus.CREATED, option1.getPrice()));

    Order order2 = orderRepository.save(
        createOrder(1L, OrderStatus.CREATED, option2.getPrice()+option3.getPrice()+option4.getPrice()));

    OrderItem orderItem1 = createOrderItem(order1, option1, 1);
    OrderItem orderItem2 = createOrderItem(order2, option2, 2);
    OrderItem orderItem3 = createOrderItem(order2, option3, 3);
    OrderItem orderItem4 = createOrderItem(order2, option4, 4);
    orderItemRepository.saveAll(List.of(orderItem1, orderItem2, orderItem3, orderItem4));

    // when
    List<OrderItem> result = orderItemRepository.findAllByOrderId(order2.getId());

    // then
    assertThat(result)
        .extracting("option.name", "quantity")
        .containsExactlyInAnyOrder(
            tuple("옵션2-2", 2),
            tuple("옵션2-3", 3),
            tuple("옵션2-4", 4)
        );

  }

  private OrderItem createOrderItem(Order order, ProductOption option, int quantity){
    return OrderItem.builder()
        .order(order)
        .price(option.getPrice())
        .option(option)
        .quantity(quantity)
        .build();
  }

  private Order createOrder(Long memberId, OrderStatus status, int amountPayment) {
    return Order.builder()
        .memberId(memberId)
        .status(status)
        .amountPayment(amountPayment)
        .build();
  }

  private ProductOption createProductOption(String name, Product product) {
    return ProductOption.builder()
        .product(product)
        .name(name)
        .price(10000)
        .build();
  }

  private Product createProduct(String name) {
    return Product.builder()
        .name(name)
        .storeId(1L)
        .storeName("store1")
        .thumbnailImgUrl("url")
        .price(10000)
        .build();
  }
}