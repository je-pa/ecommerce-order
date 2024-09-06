package com.ecommerceorder.domain.wishlist.repository;

import com.ecommerceorder.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long>, WishlistQueryDslRepository{

}
