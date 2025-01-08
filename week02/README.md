#2주차

## 회원 관리 예제
### 비즈니스 요구사항 정리
**일반적인 웹 애플리케이션 계층 구조**<br />
* 컨트롤러: 웹 MVC의 컨트롤러 역할
* 서비스: 핵심 비즈니스 로직 구현
* 리포지토리: DB에 접근, 도메인 객체를 DB에 저장하고 관리. 서비스가 동작하도록 구현
* 도메인: 비즈니스 도메인 객체. 예) 회원, 주문 등 주로 DB에 저장하고 관리됨

**클래스 의존관계**<br />
서비스 -> 리포지토리(interface로 설계) <- 메모리 구현체<br />
아직 데이터 저장소가 선정되지 않아서, 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계<br />

**회원 리포지토리 테스트 케이스**<br />
리포지토리 클래스가 원하는 대로 정상적으로 동작하는지 검증<br />
```

 package hello.hellospring.repository;
 import hello.hellospring.domain.Member;
 import org.junit.jupiter.api.AfterEach;
 import org.junit.jupiter.api.Test;
 import java.util.List;
 import java.util.Optional;
 import static org.assertj.core.api.Assertions.*;
 class MemoryMemberRepositoryTest {
 MemoryMemberRepository repository = new MemoryMemberRepository();
    @AfterEach
 public void afterEach() {
        repository.clearStore();
    }
    @Test
 public void save() {
 //given
 Member member = new Member();
        member.setName("spring");
 //when
        repository.save(member);
 //then
 Member result = repository.findById(member.getId()).get();
 assertThat(result).isEqualTo(member);
    }
    @Test
 public void findByName() {
 //given
 Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
 Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
 //when
 Member result = repository.findByName("spring1").get();
 //then
 assertThat(result).isEqualTo(member1);
    }
    @Test
 public void findAll() {
 //given
 Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
 Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
 //when
 List<Member> result = repository.findAll();
 //then
 assertThat(result.size()).isEqualTo(2);
    }
 }
```
---
## 스프링 빈과 의존관계
### 스프링 빈을 등록하는 2가지 방법
* ** 컴포넌트 스캔과 자동 의존관계 설정 **
의존성 주입: 객체 의존관계를 외부에서 넣어주는 것<br />
    - 생성자에 `@Autowired`가 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어줌
`@Component` 애노테이션이 있으면 스프링 빈으로 자동 등록됨<br />
    - `@Controller` `Service` `Repository` 애노테이션들은 `@Component`를 포함하므로 스프링 빈으로 자동 등록
컨트롤러를 통해서 외부 요청 받음->서비스에서 비즈니스 로직 만듦->리포지토리에서 데이터를 저장<br />

* **자바 코드로 직접 스프링 빈 등록**
`@Configuration` 애노테이션을 사용하여 스프링이 인식, 스프링 빈에 등록해줌<br />

---
DI에는 필드 주입, setter 주입, 생성자 주입 3가지 방법이 있음<br />
    - 의존관계가 실행 중에 동적으로 변하는 경우는 거의 없으므로 생성자 주입 권장
실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용<br />
직접 스프링 빈 등록의 장점:<br />
    - 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 할 때 해당 설정 파일만 수정하면 됨
