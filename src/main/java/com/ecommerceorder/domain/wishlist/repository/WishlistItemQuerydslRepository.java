package com.ecommerceorder.domain.wishlist.repository;

import com.ecommerceorder.domain.wishlist.entity.WishlistItem;
import java.util.Optional;

public interface WishlistItemQuerydslRepository {
  Optional<WishlistItem> findWithWishlistById(Long itemId);
}
