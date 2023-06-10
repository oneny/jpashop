package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.web.BookForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping("/items/new")
  public String createForm(Model model) {

    model.addAttribute("form", new BookForm());
    return "items/createItemForm";
  }

  @PostMapping("/items/new")
  public String create(BookForm form) {

    Book book = new Book();
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);
    return "redirect:/items";
  }

  @GetMapping("/items")
  public String list(Model model) {

    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  /**
   * 상품 수정 폼
   * (Book) itemService.findOne(itemId);
   * Book인지 확실하지 않기 때문에 인스턴스 타입을 체크한 후에 형변환을 해야 안전하다.
   * Book을 중심으로 조회해야 할 일이 있으면 차라리 JPQL을 통해서 처음부터 Book 타입을 직ㄷ접 찾아서 조회하는 방법이 더 나을 수도 있다.
   * 형변환을 하거나, JPQL을 사용해서 처음부터 Book 타입을 엔티티나 DTO로 조회하는 방법이 있다. -> JPQ책 15.3.4 상속관계와 프록시 부분 참고
   */
  @GetMapping(value = "/items/{itemId}/edit")
  public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
    Book item = (Book) itemService.findOne(itemId);
    BookForm form = new BookForm();
    form.setId(item.getId());
    form.setName(item.getName());
    form.setPrice(item.getPrice());
    form.setAuthor(item.getAuthor());
    form.setStockQuantity(item.getStockQuantity());
    form.setIsbn(item.getIsbn());

    model.addAttribute("form", form);
    return "items/updateItemForm";
  }

  @PostMapping("/items/{itemId}/edit")
  public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {
//    Book book = new Book();
//    book.setId(form.getId());
//    book.setName(form.getName());
//    book.setPrice(form.getPrice());
//    book.setStockQuantity(form.getStockQuantity());
//    book.setAuthor(form.getAuthor());
//    book.setIsbn(form.getIsbn());
//    itemService.saveItem(book);

    // 수정할 폼만 넘겨주는 방식이 더 깔끔하고 null 위험이 없다.
    // 컨트롤러에서 어설프게 엔티티 생성 X
    // 트랜잭션이 있는 서비스 계층에 식별자(id)와 변경할 데이터를 명확하게 전달
    // 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경
    // 트랜잭션 커밋 시점에 변경 감지가 실행
    itemService.updateItem(itemId,
            form.getName(),
            form.getPrice(),
            form.getStockQuantity());

    return "redirect:/items";
  }
}
