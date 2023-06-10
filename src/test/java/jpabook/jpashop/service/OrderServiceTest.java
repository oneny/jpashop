package jpabook.jpashop.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

  @PersistenceContext
  EntityManager em;

  @Autowired OrderService orderService;
  @Autowired OrderRepository orderRepository;
  private Member member;
  private Item item;
  private int orderCount;

  @BeforeEach
  public void setUp() {
    // given
    member = createMember();
    item = createBook("시골 JPA", 10000, 10);
    orderCount = 2;
  }

  @Test
  public void 상품주문() {

    // when
    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

    // then
    Order findOrder = orderRepository.findOne(orderId);

    assertAll(
            () -> assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER),
            () -> assertThat(findOrder.getOrderItems()).hasSize(1),
            () -> assertThat(item.getStockQuantity()).isEqualTo(8)
    );
  }

  @Test
  public void 상품주문_재고수량초과() {

    assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), 11))
            .isInstanceOf(NotEnoughStockException.class)
            .hasMessage("need more stock");
  }

  @Test
  public void 주문취소() {
    // when
    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
    orderService.cancelOrder(orderId);

    // then
    Order findOrder = orderRepository.findOne(orderId);

    assertAll(
            () -> assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.CANCEL),
            () -> assertThat(item.getStockQuantity()).isEqualTo(10)
    );
  }

  private Member createMember() {
    Member member = new Member();
    member.setName("회원1");
    member.setAddress(new Address("서울", "강가", "123-123"));
    em.persist(member);
    return member;
  }

  private Book createBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setStockQuantity(stockQuantity);
    book.setPrice(price);
    em.persist(book);
    return book;
  }
}
