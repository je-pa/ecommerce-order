/**
 * @Date : 2024. 08. 28.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.global.scheduler;

import com.ecommerceorder.domain.order.entity.Order;
import com.ecommerceorder.domain.order.entity.OrderItem;
import com.ecommerceorder.domain.order.repository.OrderItemRepository;
import com.ecommerceorder.domain.order.repository.OrderRepository;
import com.ecommerceorder.domain.order.type.OrderStatus;
import com.ecommerceorder.global.feign.product.dto.UpdateQuantityByProductOptionsDto;
import com.ecommerceorder.global.feign.product.service.ProductFeignService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final ProductFeignService productFeignService;

  @Scheduled(cron = "0 0 0 * * *") // 매일 0시에 실행
  public void processOrders() {
    LocalDateTime now = LocalDateTime.now();
    processShippingOrders(now);
    processShippedOrders(now);
    processReturnedOrders(now);
  }

  private void processShippingOrders(LocalDateTime now) {
    LocalDateTime oneDayAgo = now.minusDays(1);
    List<Order> ordersToShip = orderRepository.findByStatusAndCreatedDateTimeBefore(OrderStatus.CREATED, oneDayAgo);

    for (Order order : ordersToShip) {
      order.setStatus(OrderStatus.SHIPPING);
      log.info("Order {} has been updated to SHIPPING", order.getId());
    }
    orderRepository.saveAll(ordersToShip);
  }

  private void processShippedOrders(LocalDateTime now) {
    LocalDateTime twoDaysAgo = now.minusDays(2);
    List<Order> ordersToComplete = orderRepository.findByStatusAndCreatedDateTimeBefore(OrderStatus.SHIPPING, twoDaysAgo);

    for (Order order : ordersToComplete) {
      order.setStatus(OrderStatus.SHIPPED);
      log.info("Order {} has been updated to SHIPPED", order.getId());
    }
    orderRepository.saveAll(ordersToComplete);
  }

  @Transactional
  public void processReturnedOrders(LocalDateTime now) {
    LocalDateTime oneDayAgo = now.minusDays(1);
    List<Order> ordersToReturn = orderRepository.findByStatusAndModifiedDateTimeBefore(OrderStatus.REQUESTED_RETURN, oneDayAgo);

    for (Order order : ordersToReturn) {
      order.setStatus(OrderStatus.RETURNED);
      log.info("Order {} has been updated to RETURNED", order.getId());
      List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());
      productFeignService.updateProductStock(UpdateQuantityByProductOptionsDto.from(orderItems));
    }
    orderRepository.saveAll(ordersToReturn);
  }
}
