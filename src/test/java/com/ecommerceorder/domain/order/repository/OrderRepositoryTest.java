/**
 * @Date : 2024. 08. 28.
 * @author : jieun(je-pa)
 */
package com.ecommerceorder.domain.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.ecommerceorder.IntegrationTestSupport;
import com.ecommerceorder.domain.order.entity.Order;
import com.ecommerceorder.domain.order.type.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class OrderRepositoryTest extends IntegrationTestSupport {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @DisplayName("상태와 특정 시간 이전의 생성된 주문을 조회한다.")
  @Test
  void findByStatusAndCreatedDateTimeBefore(){
    // given
    LocalDateTime modifiedSampleDateTime = LocalDateTime.of(2024, 8, 29, 0, 0);

    insertOrder(1L, OrderStatus.CREATED,
        LocalDateTime.of(2024, 8, 28, 0, 0), modifiedSampleDateTime);
    insertOrder(1L, OrderStatus.CREATED,
        LocalDateTime.of(2024, 8, 27, 0, 0), modifiedSampleDateTime);
    insertOrder(1L, OrderStatus.CREATED,
        LocalDateTime.of(2024, 8, 29, 0, 0), modifiedSampleDateTime);
    insertOrder(1L, OrderStatus.CREATED,
        LocalDateTime.of(2024, 8, 28, 0, 1), modifiedSampleDateTime);
    insertOrder(1L, OrderStatus.CREATED,
        LocalDateTime.of(2024, 8, 27, 23, 59), modifiedSampleDateTime);
    insertOrder(1L, OrderStatus.RETURNED,
        LocalDateTime.of(2024, 8, 27, 0, 0), modifiedSampleDateTime);

    // when
    List<Order> result = orderRepository.findByStatusAndCreatedDateTimeBefore(
        OrderStatus.CREATED,
        LocalDateTime.of(2024, 8, 28, 0, 0));

    // then
    assertThat(result).hasSize(2)
        .extracting("status", "createdDateTime", "modifiedDateTime")
        .containsExactlyInAnyOrder(
            tuple(OrderStatus.CREATED, LocalDateTime.of(2024, 8, 27, 0, 0), modifiedSampleDateTime),
            tuple(OrderStatus.CREATED, LocalDateTime.of(2024, 8, 27, 23, 59), modifiedSampleDateTime)
        );

  }

  @DisplayName("상태와 특정 시간 이전에 수정된 주문을 조회한다.")
  @Test
  void findByStatusAndModifiedDateTimeBefore(){
    // given
    LocalDateTime createdDateTime = LocalDateTime.of(2024, 8, 27, 0, 0);

    insertOrder(1L, OrderStatus.SHIPPING,
        createdDateTime, LocalDateTime.of(2024, 8, 28, 0, 0));
    insertOrder(1L, OrderStatus.CREATED,
        createdDateTime, LocalDateTime.of(2024, 8, 27, 0, 0));
    insertOrder(1L, OrderStatus.SHIPPED,
        createdDateTime, LocalDateTime.of(2024, 8, 29, 0, 0));
    insertOrder(1L, OrderStatus.SHIPPED,
        createdDateTime, LocalDateTime.of(2024, 8, 28, 0, 1));
    insertOrder(1L, OrderStatus.SHIPPED,
        createdDateTime, LocalDateTime.of(2024, 8, 29, 0, 1));
    insertOrder(1L, OrderStatus.CREATED,
        createdDateTime, LocalDateTime.of(2024, 8, 27, 23, 59));
    insertOrder(1L, OrderStatus.RETURNED,
        createdDateTime, LocalDateTime.of(2024, 8, 30, 0, 0));

    // when
    List<Order> result = orderRepository.findByStatusAndModifiedDateTimeBefore(
        OrderStatus.SHIPPED,
        LocalDateTime.of(2024, 8, 29, 0, 0));

    // then
    assertThat(result).hasSize(1)
        .extracting("status", "createdDateTime", "modifiedDateTime")
        .containsExactlyInAnyOrder(
            tuple(OrderStatus.SHIPPED,
                createdDateTime,
                LocalDateTime.of(2024, 8, 28, 0, 1))
        );

  }

  // AuditingEntityListener 관계없이 실행되게 jdbc를 사용
  private void insertOrder(Long memberId, OrderStatus status, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
    String sql = "INSERT INTO orders (member_id, status, amount_payment, created_date_time, modified_date_time) " +
        "VALUES (?, ?, ?, ?, ?)";

    jdbcTemplate.update(sql, memberId, status.toString(), 10000, createdDateTime, modifiedDateTime);
  }

}