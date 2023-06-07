package jpabook.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Test
  @Transactional
  @Rollback(false)
  public void testMember() {
    Member member = new Member();
    member.setUsername("memberA");
    Long saveId = memberRepository.save(member);

    Member findMember = memberRepository.find(saveId);

    org.junit.jupiter.api.Assertions.assertAll(
            () -> assertThat(findMember.getId()).isEqualTo(member.getId()),
            () -> assertThat(findMember.getUsername()).isEqualTo(member.getUsername()),
            () -> assertThat(findMember).isEqualTo(member)
    );
  }
}
