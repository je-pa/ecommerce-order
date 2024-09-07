/**
 * @Date : 2024. 08. 26.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.domain.wishlist.repository;

import static com.ecommerceorder.domain.product.entity.QProduct.product;
import static com.ecommerceorder.domain.product.entity.QProductOption.productOption;
import static com.ecommerceorder.domain.wishlist.entity.QWishlist.wishlist;
import static com.ecommerceorder.domain.wishlist.entity.QWishlistItem.wishlistItem;

import com.ecommerceorder.domain.wishlist.entity.Wishlist;
import com.ecommerceorder.domain.wishlist.repository.dao.QWishlistWithItemsDao;
import com.ecommerceorder.domain.wishlist.repository.dao.QWishlistWithItemsDao_Item;
import com.ecommerceorder.domain.wishlist.repository.dao.WishlistWithItemsDao;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WishlistQueryDslRepositoryImpl implements WishlistQueryDslRepository{

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Wishlist> findByMemberIdAndProductId(Long memberId, Long productId) {
    Wishlist result = queryFactory
        .selectFrom(wishlist)
        .where(
            wishlist.memberId.eq(memberId),
            wishlist.product.id.eq(productId)
        )
        .fetchOne();

    return Optional.ofNullable(result);
  }

  @Override
  public List<WishlistWithItemsDao> findWishlistWithItemsDaoListBy(Long memberId) {
    return queryFactory
        .select(new QWishlistWithItemsDao(
            wishlist.id,
            product.id,
            product.name,
            product.storeId,
            product.storeName,
            wishlist.memberId,
            new QWishlistWithItemsDao_Item(
                wishlistItem.id,
                productOption.id,
                productOption.name,
                wishlistItem.quantity
            )
        ))
        .from(wishlist)
        .join(wishlist.product, product)
        .join(wishlistItem).on(wishlistItem.wishlist.eq(wishlist))
        .join(wishlistItem.option, productOption)
        .where(wishlist.memberId.eq(memberId))
        .fetch();
  }

}
