package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  @Transactional
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  @Transactional
  public void updateItem(Long  itemId, String name, int price, int stockQuantity) { // itemParam: 파라미터로 넘어온 준영속 상태의 엔티티
    Item findItem = itemRepository.findOne(itemId);

    // 변경감지로 setter를 사용하기 보다는 엔티티에서 추적할 수 있는 메서드를 만드는 것이 좋다.
    findItem.setName(name);
    findItem.setPrice(price);
    findItem.setStockQuantity(stockQuantity);
  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findOne(Long itemId) {
    return itemRepository.findOne(itemId);
  }
}
