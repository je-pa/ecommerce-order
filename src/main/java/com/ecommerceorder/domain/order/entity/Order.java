package com.ecommerceorder.domain.order.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.ecommerceorder.domain.BaseEntity;
import com.ecommerceorder.domain.order.type.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "amount_payment", nullable = false)
  private int amountPayment;

  @Column(name = "confirmed_date_time", nullable = true)
  private LocalDateTime confirmedDateTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @Setter
  private OrderStatus status;

  @Builder
  public Order(int amountPayment, Long memberId, OrderStatus status) {
    this.amountPayment = amountPayment;
    this.memberId = memberId;
    this.status = status;
  }

  public void requestCancel() {
    this.status = OrderStatus.CANCELLED;
  }

  public void requestReturn() {
    this.status = OrderStatus.REQUESTED_RETURN;
  }
}
