package com.ecommerceorder.domain.wishlist.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.ecommerceorder.IntegrationTestSupport;
import com.ecommerceorder.domain.product.entity.Product;
import com.ecommerceorder.domain.product.entity.ProductOption;
import com.ecommerceorder.domain.product.repository.ProductOptionRepository;
import com.ecommerceorder.domain.product.repository.ProductRepository;
import com.ecommerceorder.domain.wishlist.entity.Wishlist;
import com.ecommerceorder.domain.wishlist.entity.WishlistItem;
import com.ecommerceorder.domain.wishlist.repository.dao.WishlistWithItemsDao;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class WishlistRepositoryTest extends IntegrationTestSupport {

  @Autowired
  private WishlistItemRepository wishlistItemRepository;

  @Autowired
  private WishlistRepository wishlistRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductOptionRepository productOptionRepository;

  @DisplayName("memberId, productId로 위시리스트를 조회할 수 있다.")
  @Test
  void findByMemberIdAndProductId(){
    // given
    Product product1 = productRepository.save(createProduct("상품1"));
    Product product2 = productRepository.save(createProduct("상품2"));
    Product product3 = productRepository.save(createProduct("상품3"));
    Product product4 = productRepository.save(createProduct("상품4"));

    List<Wishlist> wishlists = new ArrayList<>();
    wishlists.add(createWishlist(1L, product1));
    wishlists.add(createWishlist(1L, product2));
    wishlists.add(createWishlist(1L, product3));
    wishlists.add(createWishlist(2L, product3));
    wishlists.add(createWishlist(2L, product4));
    wishlistRepository.saveAll(wishlists);

    // when
    Wishlist wishlist = wishlistRepository.findByMemberIdAndProductId(1L,
        product1.getId()).orElseGet(null);

    // then
    assertThat(wishlist)
        .extracting("memberId", "product.name")
        .contains(1L, "상품1");
  }

  @DisplayName("memberId로 위시리스트와 위시리스트 항목을 조회할 수 있다.")
  @Test
  void findWishlistWithItemsDaoListBy(){
    // given
    Product product1 = productRepository.save(createProduct("상품1"));
    Product product2 = productRepository.save(createProduct("상품2"));
    Product product3 = productRepository.save(createProduct("상품3"));

    Wishlist wishlist1 = wishlistRepository.save(createWishlist(1L, product1));
    Wishlist wishlist2 = wishlistRepository.save(createWishlist(1L, product2));
    Wishlist wishlist3 = wishlistRepository.save(createWishlist(2L, product3));
    Wishlist wishlist4 = wishlistRepository.save(createWishlist(2L, product1));

    ProductOption option1 = productOptionRepository.save(createProductOption("옵션1-1", product1));
    ProductOption option2 = productOptionRepository.save(createProductOption("옵션1-2", product1));
    ProductOption option3 = productOptionRepository.save(createProductOption("옵션1-3", product1));
    ProductOption option4 = productOptionRepository.save(createProductOption("옵션2-1", product2));
    ProductOption option5 = productOptionRepository.save(createProductOption("옵션3-1", product3));

    List<WishlistItem> items = new ArrayList<>();
    items.add(createWishlistItem(wishlist1, option1));
    items.add(createWishlistItem(wishlist1, option2));
    items.add(createWishlistItem(wishlist1, option3));
    items.add(createWishlistItem(wishlist2, option4));
    items.add(createWishlistItem(wishlist3, option5));
    items.add(createWishlistItem(wishlist4, option1));
    wishlistItemRepository.saveAll(items);

    // when
    List<WishlistWithItemsDao> wishlistWithItemsDaoListBy = wishlistRepository
        .findWishlistWithItemsDaoListBy(1L);

    // then
    assertThat(wishlistWithItemsDaoListBy).hasSize(4)
        .extracting("productName", "item.optionName")
        .containsExactly(
            tuple("상품1" , "옵션1-1"),
            tuple("상품1" , "옵션1-2"),
            tuple("상품1" , "옵션1-3"),
            tuple("상품2" , "옵션2-1")
        );
  }
  private WishlistItem createWishlistItem(Wishlist wishlist, ProductOption productOption) {
    return WishlistItem.builder()
        .wishlist(wishlist)
        .option(productOption)
        .quantity(5)
        .build();
  }

  private Wishlist createWishlist(Long memberId, Product product){
    return Wishlist.builder()
        .memberId(memberId)
        .product(product)
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