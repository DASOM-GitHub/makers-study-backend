# 2주차 -섹션 4. 회원 관리 예쩨 - 백엔드 개발
<br>

#### 비즈니스 요구 사항

- 데이터: 회원 ID, 이름<br>
- 기능: 회원 등록, 조회<br>
- 아직 DB가 정해지지 않았다는 가상 시나리오 부여<br>

#### 일반적인 웹 애플리케이션 계층 구조

- 컨트롤러: 웹 MVC의 컨트롤러 역할<br>
- 서비스: 핵심 비즈니스 로직 구현<br>
- 리포지토리: 데이터베이스에 접근, 도메인, 객체를 DB에 저장하고 관리<br>
- 도메인: 비즈니스 도메인 객체.<br>
    예시: 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨.<br>

#### 클래스 의존 관계
- 아직 데이터 저장소가 선정되지 않아 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계<br>

domain/Member.java(Class)


```
package hello.hello.spring.domain;

public class Member {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


```

repository/Memberrepository.java(interface)


```
import hello.hello.spring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();

}
```

repository/MemoryMemberRepository.java(class)

```
package hello.hello.spring.repository;

import java.util.HashMap;
import java.util.Optional;

public class MemoryMemberRepository implements  MemberRepository {

    private static Map<Long,Member> store = new HashMap<>();
    private static long sequence = 0L;


    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(Id));
    }

    @Override
    public Optional<Member> findById(String name) {
        store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //member 반환
    }
}

```



#### 회원 리포지토리 테스트
- 개발한 기능을 실행해서 테스트 할 때 자바의 main 메서드를 통해서 실행하거나 웹 애플리케이션의 컨트롤러를 통해서 해당 기능을 실행한다.<br>
- 단점: 실행하는 데에 오래 걸리고, 반복 실행이 어려우며 여러 테스트를 한 번에 실행하기 어렵다. → JUnit이라는 프레임워크로 테스트를 해 이러한 문제 해결<br>
- 각각의 테스트는 @Test 어노테이션을 통해 해당 메소드가 단위 테스트 메소드임을 지정<br>
- save 메소드는 spring이라는 이름을 가진 회원을 리포지토리에 저장.<br>

src/test/java 하위 폴더에 생성

MemoryMemberRepositoryTest.java

```
package hello.hello.spring.repository;

import hello.hello.spring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
    repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);


        Member result = repository.findById(member.getId()).get();
        Assertions.assertEquals(member, result);
        // Assertions.assertThat(member).isEqualTo(result);
        //assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2" ); //shift+f6 == rename
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(3);

    }

}


```

*모든 테스트는 순서와 (의존)관계없이 메소드별로 다 따로 동작하게 설계해야 함. —> 히니의 테스트가 끝날 떄마다 저장소나 공용 데이터들을 지워줘야 문제가 없음.<br>



#### 회원 서비스 개발
- join() = 회원가입 메소드로validateDuplicateMember() 로 동일한 이름을 가진 회원이 이미 있는지 검사하고memberRepository 에 전달받은 회원을 저장한다.<br>



```
package hello.hello.spring.service;

import hello.hello.spring.domain.Member;
import hello.hello.spring.repository.MemberRepository;
import hello.hello.spring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;
    public  MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //회원 가입
    public Long join(Member member) {
    //같은 이름이 있는 중복 회원 X
        validateDuplicateMember(member); // 중복 회원 검증

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }

    //전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
```

#### 회원 서비스 테스트

##### 의존성 주입
- 객체가 의존하는 또 다른 객체를 외부에서 선언하고 이를 주입받아 사용. <br>
- 테스트 상태 초기화<br>


```
package hello.hello.spring.service;

import hello.hello.spring.domain.Member;
import hello.hello.spring.repository.MemberRepository;
import hello.hello.spring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;
    public  MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //회원 가입
    public Long join(Member member) {
    //같은 이름이 있는 중복 회원 X
        validateDuplicateMember(member); // 중복 회원 검증

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }

    //전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
```
# 섹션 5. 스프링 빈과 의존 관계

### 스프링 빈
__스프링 빈을 등록하는 2가지 방법__ <br>
- 컴포넌트 스캔과 자동 의존관계 설정<br>
- 자바 코드로 직접 스프링 빈 등록하기<br>

__컴포넌트 스캐노가 자동 의존관계 설정__
- @Component : 애노테이션이 있으면 스프링 빈으로 자동 등록된다. <br>
- @Controller : 컨트롤러가 스프링 빈으로 자동 등록된 이유도 컴포넌트 스캔 때문이다. <br>

- @Component를 포함하는 다음 애노테이션도 스트링 빈으로 자동 등록된다. <br>
1. @Controller
2. @Service
3. @Repository

### 직접 스프링 빈 등록하기
/hello.hellospring/SpringConfig.java

```
@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
```

- memberRepository를 스프링 빈에 등록하고 memberService에 주입
- memberService 스프링 빈에 등록.
