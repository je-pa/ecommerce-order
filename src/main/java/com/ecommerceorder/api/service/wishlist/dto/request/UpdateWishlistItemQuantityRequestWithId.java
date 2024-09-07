package com.ecommerceorder.api.service.wishlist.dto.request;

import com.ecommerceorder.api.controller.wishlist.dto.request.Action;
import lombok.Builder;

@Builder
public record UpdateWishlistItemQuantityRequestWithId(
    Action action,
    Long itemId,
    Long currentMemberId
) {


}