package com.ecommerceorder.domain.product.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.ecommerceorder.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products_for_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "product_id")
  private Long id;

  @Column(name = "store_id", nullable = false)
  private Long storeId;

  @Column(name = "store_name", nullable = false)
  private String storeName;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "thumbnail_img_url")
  private String thumbnailImgUrl;

  @Column(name = "price", nullable = false)
  private int price;

  @Builder
  public Product(String name, Long storeId, String storeName, String thumbnailImgUrl, int price) {
    this.name = name;
    this.storeId = storeId;
    this.storeName = storeName;
    this.thumbnailImgUrl = thumbnailImgUrl;
    this.price = price;
  }
}
