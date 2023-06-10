package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

  private final EntityManager em;

  /**
   * 병합시 주의
   * 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만,
   * 병합을 사용하면 모든 속성이 변경된다.
   * 병합시 값이 없으면 null로 업데이트할 위험도 있다.(병합은 모든 필드를 교체하기 때문에)
   */
  public void save(Item item) {
    if (item.getId() == null) {
      em.persist(item);
    } else {
      // merge하게 되면 영속성 컨텍스트는 item을 관리하지 않고,
      // 변경된 mergeItem을 관리하여 반환한다.
      Item mergeItem = em.merge(item);
    }
  }

  public Item findOne(Long id) {
    return em.find(Item.class, id);
  }

  public List<Item> findAll() {
    return em.createQuery("select i from Item i", Item.class)
            .getResultList();
  }
}
