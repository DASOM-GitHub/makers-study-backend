# 2주차 - 회원 관리 예제, 스프링 빈과 의존관계

### 일반적인 웹 애플리케이션 계층 구조
##### 컨트롤러, 서비스, 리포지토리, DB, 도메인
###### 컨트롤러 : 웹 MVC의 컨트롤러 역할
###### 서비스 : 핵심 비즈니스 로직 구현
###### 리포지토리 : DB에 접근, 도메인 객체를 DB에 저장하고 관리
###### 도메인 : 비즈니스 도메인 객체이며 주로 DB에 저장하고 관리됨(사용자 요구사항에 따른 상위 수준의 개발 범위) 예) 회원, 주문, 등

### 클래스 의존관계
##### MrS -> interface <- MyMrR
###### 데이터 저장소가 선정되지 않으면 인터페이스로 구현 클래스를 변경할 수 있도록 설계

### 회원 리포지토리 인터페이스
##### Optional
###### : Null이 될 수도 있는 객체를 감싸주는 클래스
###### 효과 - 결과가 null인 경우 간단하게 처리 가능, 간결하고 안전한 코드 작성 가능
```
Optional<Member> findById(Long id);
```

### 회원 리포지토리 테스트 케이스 작성
##### @AfterEach
###### 개발한 기능 테스트 할 때 main, 컨트롤러를 통한 실행은 준비 및 실행이 오래 걸리고, 반복 실행이 어렵고, 여러 테스트를 한 번에 실행하기 어려움
###### -> 그래서 JUnit이라는 프레임워크 사용
###### scr/test/java 하위 폴더에 생성
###### 한 번에 여러 테스트를 실행하면 메모리 DB에 직전 테스트 결과 남음
###### 이렇게 되면 다음 테스트 실패 가능성 있음
###### 그래서 @AfterEach를 사용하여 각 테스트가 종료될 때마다 기능 실행 -> 메모리 DB에 저장된 데이터 삭제
```
@AfterEach
    public void afterEach() {
        repository.clearStore();
    }
```
###### 테스트를 먼저 만드는 테스트 주도 개발도 있음

### 회원 서비스 개발
##### Extract Method
###### : 그룹으로 묶어 코드 목적을 시각화하는 역할을 하며, 코드의 일부를 별도의 메소드로 분리하는 기능
###### 이해하기 쉽고 유지보수가 쉬움, 코드 일관성 유지에 도움
```
 public Long join(Member member) {
        validataDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validataDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
```

### 회원 서비스 테스트
##### @BeforeEach
###### 각 테스트 실행 전에 호출 됨, 데스트가 서로 영향이 없도록 새로운 객체를 생성, 의존관계 맺어줌
```
@BeforeEach
    public  void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }
```

### 컴포넌트 스캔과 자동 의존관계 설정
##### @Autowired
###### 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어줌
###### DI(Dependency Injection, 의존성 주입) : 객체 의존관계를 외부에서 넣어주는 것
###### 오류 - memberService가 스프링 빈으로 등록 X
###### MrS - @Service / MyMrR - @Repository 넣어주기
###### 스프링 빈을 등록하는 2가지 방법
###### 1) 컴포넌트 스캔과 자동 의존관계 설정
###### 2) 자바 코드로 직접 스프링 빈 등록
```
@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
```

### 컴포넌트 스캔 원리
###### @Component 애노테이션이 있으면 스프링 빈으로 자동 등록 - @Controller, @Service, @Repository
###### @Controller 컨트롤러가 스프링 빈으로 자동 등록된 이유 - 컴포넌트 스캔 때문
```
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
      this.memberRepository = memberRepository;
    }

}
```
```
@Repository
public class MemoryMemberRepository implements MemberRepository {}
```
###### 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때 기본으로 싱글톤으로 등록
###### 같은 스프링 빈이면 모두 같은 인스턴스

### 자바 코드로 직접 스프링 빈 등록하기
###### 회원 서비스, 리포지토리의 @Service, @Repository, @Autowired 애노테이션 제거 후 진행
###### DI에는 필트 주입, setter 주입, 생성자 주입 3가지 방법이 있음, 생성자 주입 권장
###### 정형화 되지 않거나 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록