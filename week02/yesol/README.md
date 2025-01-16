# 4~5섹션

### 비즈니스 요구사항

- 데이터 : 회원ID, 이름
- 기능 : 회원 등록, 조회
- 데이터 저장소 선정X
- MemberService 클래스, MemberRepository 인터페이스, Memory 구현체

- hello-spring > src > main > java > hello.hello_spring > domain 패키지 > **Member 클래스** 생성
    
    ```java
    package hello.hello_spring.domain;
    
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
    
- hello-spring > src > main > java > hello.hello_spring > repository 패키지 > **MemberRepository 인터페이스** 생성
    
    ```java
    package hello.hello_spring.repository;
    
    import hello.hello_spring.domain.Member;
    
    import java.util.List;
    import java.util.Optional;
    
    public interface MemberRepository {
        Member save(Member member); //회원 저장
        Optional<Member> findById(Long id);
        Optional<Member> findByName(String name);
        List<Member> findAll(); //모든 회원 리스트 반환
    }
    // Optional => null값 처리 방법
    ```
    
- hello-spring > src > main > java > hello.hello_spring > repository  > **MemoryMemberRepository 클래스** 생성
    
    ```java
    package hello.hello_spring.repository;
    
    import hello.hello_spring.domain.Member;
    
    import java.util.*;
    
    public class MemoryMemberRepository implements MemberRepository {
    
        private static Map<Long, Member> store = new HashMap<>();
        private static long sequence = 0L;
    
        @Override
        public Member save(Member member) { //id 셋팅 후 저장
            member.setId(++sequence);
            store.put(member.getId(), member);
            return member;
        }
    
        @Override
        public Optional<Member> findById(Long id) {
            return Optional.ofNullable(store.get(id)); //id가 널값이어도 반환
        }
    
        @Override
        public Optional<Member> findByName(String name) {
            return store.values().stream()
                    .filter(member -> member.getName().equals(name))
                    .findAny(); //이름 하나 찾으면 반환
        }
    
        @Override
        public List<Member> findAll() {
            return new ArrayList<>(store.values());
        }
            
        public void clearStore() {
            store.clear();
        } // test 실행마다 메모리 비우기 용도
    }
    
    ```
    
- ***test case 작성*** (src > test > java > hello.hellospring > repository 패키지 > MemoryMemberRepositoryTest 생성)
    
    ```java
    package hello.hellospring.repositroy;
    
    import hello.hellospring.domain.Member;
    import hello.hellospring.repository.MemoryMemberRepository;
    import org.assertj.core.api.Assertions;
    import org.junit.jupiter.api.AfterEach;
    import org.junit.jupiter.api.Test;
    
    import java.util.List;
    import java.util.Optional;
    
    class MemoryMemberRepositoryTest {
    
        MemoryMemberRepository repository = new MemoryMemberRepository();
    
        @AfterEach // test 실행마다 store 비우기(실행순서 상관없이 한번에 실행확인 가능)
        public void afterEach() {
            repository.clearStore();
        } 
    
        @Test
        public void save() {
            Member member = new Member();
            member.setName("spring");
    
            repository.save(member);
    
            Member result = repository.findById(member.getId()).get();
    //      System.out.println("result = " + (result == member));
    //      Assertions.assertEquals(member, result);
            Assertions.assertThat(member).isEqualTo(result);
        }
    
        @Test
        public void findByName() {
            Member member1 = new Member();
            member1.setName("spring1");
            repository.save(member1);
    
            Member member2 = new Member();
            member2.setName("spring2");
            repository.save(member2);
    
            Member result = repository.findByName("spring1").get();
    
            Assertions.assertThat(result).isEqualTo(member1);
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
    
            Assertions.assertThat(result.size()).isEqualTo(2);
        }
    }
    
    ```
    

- 회원 서비스 개발 (src > test > java > hello.hellospring > service 패키지 > MemberService 생성 )
    
    ```java
    package hello.hellospring.service;
    
    import hello.hellospring.domain.Member;
    import hello.hellospring.repository.MemberRepository;
    import hello.hellospring.repository.MemoryMemberRepository;
    
    import java.util.List;
    import java.util.Optional;
    
    public class MemberService {
    
        private final MemberRepository memberRepository = new MemoryMemberRepository();
    
        /*
        * 회원 가입
        */
        public Long join(Member member) {
            //같은 이름이 있는 중복 회원 X
            /*Optional<Member> result = memberRepository.findByName(member.getName()); //ctrl+alt+v로 변수도입기능 사용가능
            result.ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다."); 
            });*/
    		        //Optional 메소드들 중 하나 여서 아래 매소드 안의 코드처럼 바로 작성가능
    
            valiateDuplicateMember(member); //중복회원관리 (ctrl+t로 단축키검색 -> ctrl+alt+m => 자동 메소드생성
            memberRepository.save(member);
            return member.getId();
        }
    
        private void valiateDuplicateMember(Member member) {
            memberRepository.findByName(member.getName())
                    .ifPresent(m -> {
                        throw new IllegalStateException("이미 존재하는 회원입니다.");
                    });
        }
    
        /*
         * 전체 회원 조회
         */
        public List<Member> findMembers(){
            return memberRepository.findAll();
        }
    
        public Optional<Member> findOne(Long memberId){
            return memberRepository.findById(memberId);
        }
    }
    
    ```
    
- 회원 서비스 테스트 (클래스 이름 잡고 ctrl+shift+t ⇒ 자동 test 클래스 생성)
    
    ```java
    package hello.hellospring.service;
    
    import hello.hellospring.domain.Member;
    import hello.hellospring.repository.MemoryMemberRepository;
    import org.assertj.core.api.Assertions;
    import org.junit.jupiter.api.AfterEach;
    import org.junit.jupiter.api.Test;
    
    import static org.assertj.core.api.Assertions.*;
    import static org.junit.jupiter.api.Assertions.*;
    
    class MemberServiceTest {
    
        MemberService memberService = new MemberService();
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    
        @AfterEach
        public void afterEach() {
            memberRepository.clearStore();
        } //test 실행 후 db값 초기화
    
        @Test
        void 회원가입() {
            //given 조건
            Member member = new Member();
            member.setName("hello");
    
            //when 무엇을
            Long saveId = memberService.join(member);
    
            //then 검증
            Member findMember = memberService.findOne(saveId).get();
            assertThat(member.getName()).isEqualTo(findMember.getName());
            //Assertions를 static으로 변환 (alt+enter)
        }
    
        @Test
        public void 중복_회원_예외() {
            //given
            Member member1 = new Member();
            member1.setName("spring");
            Member member2 = new Member();
            member2.setName("spring");
    
            //when
            memberService.join(member1);
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
            // 얘가 되야한다 -> 이게 실행될때
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    
            /*try {
                memberService.join(member2);
                org.junit.jupiter.api.Assertions.fail(); //그냥 fail()하면 에러남
            } catch (IllegalStateException e) {
                assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
            } try_catch로 가능하지만 굳이?*/
    
            //then
        }
    
        @Test
        void findMembers() {
        }
    
        @Test
        void findOne() {
        }
    }
    ```
    
    - 테스트를 같은 레퍼지토리로 사용하기 위해서 MemberService, MemberServiceTest 클래스 수정
        
        ```java
        public class MemberService {
        
            private final MemberRepository memberRepository;
            
            public MemberService(MemberRepository memberRepository) {
                this.memberRepository = memberRepository;
            } //외부에서 넣어주도록 설정
        ```
        
        ```java
        class MemberServiceTest {
        
            MemberService memberService;
            MemoryMemberRepository memberRepository;
        
            @BeforeEach
            public void beforeEach() {
                memberRepository  = new MemoryMemberRepository();
                memberService = new MemberService(memberRepository);
            } //테스트 실행전 마다 같은 메모리레포지토리를 가지도록 설정
        ```
        

---

### 의존관계

- 회원 컨트롤러 ⇒ 회원 서비스, 회원 레포지토리를 의존
- ***컴포넌트 스캔 방식***
    - src > main > java > hello.hellospring > controller > MemberController 클래스 생성
        
        ```java
        package hello.hellospring.controller;
        
        import hello.hellospring.service.MemberService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        
        @Controller
        public class MemberController {
        
            private final MemberService memberService;
        
            @Autowired //컨트롤러와 서비스를 연결(의존관계)
            public MemberController(MemberService memberService) {
                this.memberService = memberService;
            }
        }
        /*----------------------------------------------------------------*/
        @Service
        public class MemberService {
        
            private final MemberRepository memberRepository;
        
            @Autowired //서비스와 레포지토리를 연결(의존)
            public MemberService(MemberRepository memberRepository) {
                this.memberRepository = memberRepository;
            }
        /*----------------------------------------------------------------*/
        @Repository
        public class MemoryMemberRepository implements MemberRepository {
        
        ```
        
        - 스프링이 인식할 수 있도록 @Controller, @Service, @Repository 클래스명 위에 작성 
        (컴포넌트 스캔 방식)
        - 의존관계는 @Autowired로 연결

- ***자바 코드로 직접 스프링 빈 등록***
    - @Service, (sevice)@Autowired, @Repository 제거
    - main > java > hello.hellospring > service > SpringConfig 클래스 생성
        
        ```java
        package hello.hellospring.service;
        
        import hello.hellospring.repository.MemberRepository;
        import hello.hellospring.repository.MemoryMemberRepository;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        
        @Configuration //스프링이 뜰때 읽음
        public class SpringConfig {
        
            @Bean //스프링 빈에 등록
            public MemberService memberService() {
                return new MemberService(memberRepository()); //서비스 -> 레포지토리
            }
        
            @Bean
            public MemberRepository memberRepository() {
                return new MemoryMemberRepository();
            }
        }
        ```
        
        - 나중에 db연결 시 간편한 방식
- DI 방식
    
    ```java
    // 1. 생성자 주입 (권장-의존관계가 동적으로 변할 일 없음)
    @Controller
    public class MemberController {
    
        private final MemberService memberService;
    
        @Autowired
        public MemberController(MemberService memberService) {
            this.memberService = memberService;
        }
    }
    
    // 2. 필드 주입 (별로)
    @Controller
    public class MemberController {
    
        @Autowired private MemberService memberService;
    }
    
    // 3. 셋터 주입 (메소드가 public로 열려있음)
    @Controller
    public class MemberController {
    
        private MemberService memberService;
    
        @Autowired
        public void setMemberController(MemberService memberService) {
            this.memberService = memberService;
        }
    }
    ```