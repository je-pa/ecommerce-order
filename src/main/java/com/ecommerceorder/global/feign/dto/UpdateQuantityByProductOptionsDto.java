/**
 * @Date : 2024. 08. 28.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.global.feign.dto;

import com.ecommerceorder.domain.order.entity.OrderItem;
import java.util.Collection;

public record UpdateQuantityByProductOptionsDto(
    Collection<ProductOptionInfo> productOptionInfos
) {

  public record ProductOptionInfo (Long getProductOptionId, int getQuantity){
    public static ProductOptionInfo from(OrderItem item) {
      return new ProductOptionInfo(item.getId(),item.getQuantity());
    }
  }

  public static UpdateQuantityByProductOptionsDto from(Collection<OrderItem> orderItems) {
    return new UpdateQuantityByProductOptionsDto(
        orderItems.stream().map(ProductOptionInfo::from).toList());
  }
}
