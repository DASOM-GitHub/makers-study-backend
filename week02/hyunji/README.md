## 2주차 - 회원 관리 예제

### 비즈니스 요구사항
- 데이터(회원ID, 이름)
- 기능(회원 등록, 조회)


### 일반적 웹 애플리케이션 계층 컨트롤러, 서비스 리포지토리, 도메인, DB로 구성
1. 컨트롤러 : 웹 MVC의 컨트롤러 역할
2. 서비스 : 핵심 비즈니스 로직 구현 ex. 중복가입 불가
3. 리포지토리 : 데이터베이스에 접근, 도메인 객체를 DB에 저장 및 관리
4. 도메인 : 비즈니스 도메인 객체


### 데이터 저장소가 선정되지 않았으므로 인터페이스로 구현 클래스 변경 가능하도록 설계


### 회원 리포지토리 테스트 케이스 작성    
개발한 기능을 테스트 할 때 자바의 main 메서드나 웹 애플리케이션의 컨트롤러를 통해 해당 기능을 실행. 하지만 오래 걸리고 반복 실행하기 어렵고 여러 테스트를 한번 에 실행하기 어려움. JUnit이라는 프레임워크로 테스트를 실행해 문제 해결. 


### 테스트 코드 예제
<pre>
<code>
package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    public void 회원가입() throws Exception {
//Given
        Member member = new Member();
        member.setName("hello");
//When
        Long saveId = memberService.join(member);
//Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
//Given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");
//When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2)); //예외가 발생해야 한다.

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        /*
        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
        */
    }
}
</code>
</pre>


### @AfterEach : 각각의 테스트들은 작성된 순서대로 실행되는게 아닌 독립적으로 실행되기 때문에 이전 테스트의 결과로 인해 다음 테스트가 정상적으로 작동되지 않을 가능성 존재. @AfterEach 사용해 각 테스트가 종료될 때 마다 메모리에 저장된 데이터 삭제.

### @BeforeEach : 테스트들이 서로 영향을 주지 않게끔 테스트 실행전에 호출되어 항상 새로운 객체를 새로 만듦.


### +given, when, then
- given : 기반으로 하는 데이터
- when : 검증할 부분
- then : 검증

## 2주차 - 스프링 빈과 의존관계

### 스프링 빈을 등록하는 2가지 방법
1. 컴포넌트 스캔과 자동 의존관계 설정 
- 컨트롤러 -> 서비스 -> 리포지토리
- @Autowired 통해 서로 연결(의존성 주입)
2. 자바 코드로 직접 스프링 빈 등록
- 컴포넌트 스캔 방식 대신에 자바 코드로 스프링 빈을 설정(@Bean)
