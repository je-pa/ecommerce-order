/**
 * @Date : 2024. 08. 28.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.domain.order.repository;

import com.ecommerceorder.domain.order.entity.OrderItem;
import java.util.List;

public interface OrderItemQueryDslRepository {
  List<OrderItem> findAllByOrderId(Long orderId);
}
