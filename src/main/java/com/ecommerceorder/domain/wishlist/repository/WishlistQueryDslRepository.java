/**
 * @Date : 2024. 08. 26.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.domain.wishlist.repository;

import com.ecommerceorder.domain.wishlist.entity.Wishlist;
import com.ecommerceorder.domain.wishlist.repository.dao.WishlistWithItemsDao;
import java.util.List;
import java.util.Optional;

public interface WishlistQueryDslRepository {
  Optional<Wishlist> findByMemberIdAndProductId(Long memberId, Long productId);

  List<WishlistWithItemsDao> findWishlistWithItemsDaoListBy(Long memberId);
}
