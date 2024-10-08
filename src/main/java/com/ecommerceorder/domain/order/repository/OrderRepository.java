package com.ecommerceorder.domain.order.repository;

import com.ecommerceorder.domain.order.entity.Order;
import com.ecommerceorder.domain.order.type.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByStatusAndCreatedDateTimeBefore(OrderStatus orderStatus, LocalDateTime dateTime);

  List<Order> findByStatusAndModifiedDateTimeBefore(OrderStatus orderStatus, LocalDateTime dateTime);
}
