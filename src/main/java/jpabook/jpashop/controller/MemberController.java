package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.web.MemberForm;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping(value = "/members/new")
  public String createForm(Model model) {
    model.addAttribute("memberForm", new MemberForm());
    return "members/createMemberForm";
  }

  @PostMapping(value = "/members/new")
  public String create(@Valid MemberForm form, BindingResult result) {

    if (result.hasErrors()) {
      return "members/createMemberForm";
    }

    Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
    Member member = new Member();
    member.setName(form.getName());
    member.setAddress(address);

    memberService.join(member);
    return "redirect:/";
  }

  /**
   * 폼 객체 vs 엔티티 직접 사용
   * 화면 요구사항이 복잡해지기 시작하면, 엔티티에 화면을 처리하기 위한 기능이 점점 증가한다.
   * 결과적으로 엔티티는 점점 화면에 종속적으로 변하고, 이렇게 화면 기능 때문에 지저분해진 엔티티는 결국 유지보수하기 어려워진다.
   *
   * 실무에서 엔티티는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 없어야 한다.
   * 화면이나 API에 맞는 폼 객체나 DTO를 사용하자. 그래서 화면이나 API 요구사항을 이것들로 처리하고, 엔티티는 최대한 순수하게 유지하자.
   * 그리고 절대 엔티티를 반환하지 않도록 하자. 엔티티에 중요정보가 있는 경우에는 보안에 위험이 될 수도 있다.
   */
  @GetMapping("/members")
  public String list(Model model) {
    List<Member> members = memberService.findMembers();
    model.addAttribute("members", members);
    return "members/memberList";
  }
}
