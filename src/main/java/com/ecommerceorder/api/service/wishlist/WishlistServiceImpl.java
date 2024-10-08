/**
 * @Date : 2024. 08. 26.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.api.service.wishlist;

import com.ecommerceorder.api.controller.ApiResponse;
import com.ecommerceorder.api.controller.wishlist.dto.request.CreateWishlistRequest;
import com.ecommerceorder.api.controller.wishlist.dto.response.WishlistWithItemsResponse;
import com.ecommerceorder.api.service.wishlist.dto.request.UpdateWishlistItemQuantityRequestWithId;
import com.ecommerceorder.domain.product.entity.Product;
import com.ecommerceorder.domain.product.entity.ProductOption;
import com.ecommerceorder.domain.product.repository.ProductOptionRepository;
import com.ecommerceorder.domain.wishlist.entity.Wishlist;
import com.ecommerceorder.domain.wishlist.entity.WishlistItem;
import com.ecommerceorder.domain.wishlist.repository.WishlistItemRepository;
import com.ecommerceorder.domain.wishlist.repository.WishlistRepository;
import com.ecommerceorder.global.exception.CustomException;
import com.ecommerceorder.global.exception.ExceptionCode;
import com.ecommerceorder.global.feign.user.service.UserFeignService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{
  private final WishlistRepository wishlistRepository;
  private final WishlistItemRepository wishlistItemRepository;
  private final ProductOptionRepository productOptionRepository;
  private final UserFeignService userFeignService;

  @Override
  @Transactional
  public ApiResponse<String> modify(Long memberId, List<CreateWishlistRequest> requests) {
    if(requests == null || requests.isEmpty()){
      throw CustomException.from(ExceptionCode.REQUESTS_EMPTY);
    }

    // 1. requests에서 optionId 리스트 추출
    Set<Long> optionIds = requests.stream()
        .map(CreateWishlistRequest::optionId)
        .collect(Collectors.toSet());

    if(optionIds.size() != requests.size()){
      throw CustomException.from(ExceptionCode.PRODUCT_OPTION_DUPLICATE);
    }

    // 2. ProductOptionRepository에서 해당 optionId들로 ProductOption 조회
    List<ProductOption> productOptions = productOptionRepository.findAllById(optionIds);

    if(productOptions.size() != requests.size()){
      throw CustomException.from(ExceptionCode.PRODUCT_OPTIONS_NOT_FOUND);
    }

    // 3. 모든 ProductOption의 Product ID가 동일한지 확인
    Set<Long> productIds = productOptions.stream()
        .map(productOption -> productOption.getProduct().getId())
        .collect(Collectors.toSet());

    if (productIds.size() > 1) {
      throw CustomException.from(ExceptionCode.WISHLIST_OPTIONS_SAME_PRODUCT_ONLY);
    }

    // 4. Member 및 Product에 해당하는 wishlist가 있으면 조회 없으면 생성
    Wishlist wishlist = wishlistRepository.findByMemberIdAndProductId(
            memberId, productOptions.getFirst().getProduct().getId())
        .orElseGet(() -> {
          // Member 및 Product 조회
          if(!userFeignService.existsMemberId(memberId)){
            throw CustomException.from(ExceptionCode.USER_NOT_FOUND);
          }

          Product product = productOptions.getFirst().getProduct();

          // 새로운 Wishlist 생성
          Wishlist newWishlist = new Wishlist(memberId, product);

          // Wishlist 저장
          return wishlistRepository.save(newWishlist);
        });
    List<WishlistItem> items = wishlistItemRepository.findByWishlist(wishlist);

    // 5. requests와 매칭되는 WishlistItem 처리
    List<WishlistItem> newItems = new ArrayList<>();
    for (CreateWishlistRequest request : requests) {
      ProductOption option = productOptions.stream()
          .filter(po -> po.getId().equals(request.optionId()))
          .findFirst()
          .orElseThrow(() -> CustomException.from(ExceptionCode.PRODUCT_OPTION_NOT_FOUND));

      WishlistItem existingItem = items.stream()
          .filter(item -> item.getOption().equals(option))
          .findFirst()
          .orElse(null);

      if (existingItem != null) {
        // 5-1. 매칭되는 WishlistItem이 있으면 quantity 추가
        existingItem.addQuantity(request.quantity());
      } else {
        // 5-2. 매칭되는 WishlistItem이 없으면 새로 생성
        WishlistItem newItem = new WishlistItem(option, request.quantity(), wishlist);
        newItems.add(newItem);
      }
    }
    if(!newItems.isEmpty()){
      wishlistItemRepository.saveAll(newItems);
    }

    return ApiResponse.ok("Wishlist updated successfully");
  }

  @Override
  @Transactional(readOnly = true)
  public List<WishlistWithItemsResponse> readList(Long currentMemberId, Long memberId) {
    if(!currentMemberId.equals(memberId)){
      throw CustomException.from(ExceptionCode.UNAUTHORIZED_ACCESS);
    }
    return WishlistWithItemsResponse.toListFrom(
        wishlistRepository.findWishlistWithItemsDaoListBy(memberId));
  }

  @Override
  @Transactional
  public ApiResponse<String> updateItemQuantity(UpdateWishlistItemQuantityRequestWithId request) {
    WishlistItem wishlistItem = wishlistItemRepository.findWithWishlistById(request.itemId())
        .orElseThrow(() -> CustomException.from(ExceptionCode.WISHLIST_ITEM_NOT_FOUND));

    if(!request.currentMemberId().equals(wishlistItem.getWishlist().getMemberId())){
      throw CustomException.from(ExceptionCode.UNAUTHORIZED_ACCESS);
    }

    switch (request.action()){
      case DECREASE -> {
        int quantity = wishlistItem.getQuantity();
        if(quantity < 2){
          Wishlist wishlist = wishlistItem.getWishlist();
          if(wishlistItemRepository.countByWishlist(wishlist) == 1){
            wishlistItemRepository.delete(wishlistItem);
            wishlistRepository.delete(wishlist);
          }else{
            wishlistItemRepository.delete(wishlistItem);
          }
        }else{
          wishlistItem.addQuantity(-1);
        }
      }
      case INCREASE -> wishlistItem.addQuantity(1);
      case DELETE -> {
        Wishlist wishlist = wishlistItem.getWishlist();
        if(wishlistItemRepository.countByWishlist(wishlist) == 1){
          wishlistItemRepository.delete(wishlistItem);
          wishlistRepository.delete(wishlist);
        }else{
          wishlistItemRepository.delete(wishlistItem);
        }
      }
    }

    return ApiResponse.ok("Wishlist updated successfully");
  }

}
