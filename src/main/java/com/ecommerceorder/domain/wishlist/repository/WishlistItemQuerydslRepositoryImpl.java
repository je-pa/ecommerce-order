package com.ecommerceorder.domain.wishlist.repository;

import static com.ecommerceorder.domain.wishlist.entity.QWishlist.wishlist;
import static com.ecommerceorder.domain.wishlist.entity.QWishlistItem.wishlistItem;

import com.ecommerceorder.domain.wishlist.entity.WishlistItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WishlistItemQuerydslRepositoryImpl implements WishlistItemQuerydslRepository{

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<WishlistItem> findWithWishlistById(Long itemId) {
    return Optional.ofNullable(queryFactory
        .select(wishlistItem)  // select에 주 엔티티(wishlistItem)를 명시적으로 포함
        .from(wishlistItem)
        .join(wishlistItem.wishlist, wishlist).fetchJoin()  // Wishlist와의 관계를 fetch join
        .where(wishlistItem.id.eq(itemId))
        .fetchOne());
  }
}
