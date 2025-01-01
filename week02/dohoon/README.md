## section3 - 회원 관리 예제 - 백엔드 개발

### 비즈니스 요구사항 정리
1. **데이터**
   - 회원 정보를 ID와 이름으로 구성합니다.
   - ID는 고유값으로 회원 식별에 사용되며, 이름은 사용자가 입력하는 데이터입니다.

2. **기능**
   - **회원 등록**: 새 회원 정보를 저장소에 추가합니다.
   - **회원 조회**: 저장소에서 ID나 이름으로 회원 정보를 검색하거나 전체 회원 목록을 조회합니다.

3. **계층 구조**
   - **Controller**: 사용자 요청(HTTP 요청)을 처리하고 응답을 반환합니다.
   - **Service**: 비즈니스 로직을 구현하며, 핵심 기능과 규칙을 관리합니다.
   - **Repository**: 데이터 저장 및 조회를 담당합니다. 실제 데이터베이스와 상호작용합니다.
   - **Domain**: 비즈니스 데이터를 표현하는 객체로, 데이터의 속성과 동작을 정의합니다.

---

### 회원 도메인과 리포지토리 만들기
1. **Member 클래스**
   - 회원 데이터를 표현하는 객체입니다.
   - ID와 이름을 필드로 가지고 있으며, getter와 setter로 값 접근을 제어합니다.

```java
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

2. **MemberRepository 인터페이스**
   - 저장소의 표준 동작(저장, 조회 등)을 정의합니다.
   - `save`, `findById`, `findByName`, `findAll` 메서드를 포함합니다.

```java
public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
```

3. **MemoryMemberRepository 구현체**
   - `MemberRepository` 인터페이스를 구현하여 메모리를 사용한 저장소를 제공합니다.
   - 데이터를 `HashMap`에 저장하며, 동시성 문제는 고려하지 않았습니다.

```java
public class MemoryMemberRepository implements MemberRepository{
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
```

---

### 회원 리포지토리 테스트 케이스 작성
1. **JUnit을 활용한 테스트**
   - `JUnit`은 자바에서 테스트를 작성하고 실행하기 위한 표준 라이브러리입니다.
   - 테스트 메서드는 `@Test`로 표시하며, 독립적으로 실행됩니다.

2. **테스트 메서드**
   - **`save`**: 저장된 회원이 올바르게 반환되는지 확인합니다.

```java
@Test
    public void save(){
        // given
        Member member = new Member();
        member.setName("spring");

        //when
        repository.save(member);

        //then
        Member result = repository.findById(member.getId()).get();
        assertThat(result).isEqualTo(member);
    }
```

   - **`findByName`**: 이름으로 검색한 결과가 정확한지 확인합니다.

```java
@Test
    public void findByName(){
        // given
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
```

   - **`findAll`**: 저장된 모든 회원이 정확히 반환되는지 확인합니다.

```java
@Test
    public void findAll(){
        // given
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
````

3. **테스트 격리**
   - 각 테스트가 독립적으로 실행되도록 `@AfterEach`를 사용하여 저장소를 초기화합니다.

```java
@AfterEach
    public void afterEach(){
        repository.clearStore();
    }
```

---

### 회원 서비스 개발
1. **회원 가입**
   - 새 회원 데이터를 저장하며, 중복 회원 검증을 수행합니다.

2. **중복 회원 검증**
   - 동일한 이름을 가진 회원이 이미 존재하면 예외를 발생시킵니다.
   - `Optional`의 `ifPresent` 메서드를 사용하여 검증합니다.

3. **전체 회원 조회**
   - 저장소에서 모든 회원 데이터를 반환합니다.

```java
public class MemberService {
    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public Long join(Member member){
        validateDuplicationMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicationMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
```

---

### 회원 서비스 테스트
1. **회원 가입 테스트**
   - 새 회원을 등록하고, 저장소에서 올바르게 조회되는지 확인합니다.

```java
@Test
    public void join() throws Exception{
        // given
        Member member = new Member();
        member.setName("hello");
        // when
        Long saveId = memberService.join(member);
        // then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }
```

2. **중복 회원 검증 테스트**
   - 동일한 이름으로 회원을 두 번 등록하려고 할 때 예외가 발생하는지 확인합니다.

```java
@Test
    public void validateDuplicationMember() throws Exception{
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");
        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
```

3. **의존성 주입 (DI)**
   - `MemoryMemberRepository`를 `MemberService`에 주입하여 테스트 간 독립성을 보장합니다.
   - 테스트 실행 시마다 새 객체를 생성하여 상태를 초기화합니다.

```java
@BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }
```

---

## section4 - 스프링 빈과 의존관계

### 컴포넌트 스캔과 자동 의존관계 설정
1. **스프링 빈 등록**
   - `@Controller`, `@Service`, `@Repository` 애노테이션이 붙은 클래스는 자동으로 스프링 빈으로 등록됩니다.
   - 이들 애노테이션은 모두 `@Component` 애노테이션을 포함하고 있어 컴포넌트 스캔의 대상이 됩니다.

2. **의존성 주입 (DI)**
   - `@Autowired`를 사용하여 스프링 컨테이너에서 필요한 의존 객체를 주입받습니다.
   - 생성자 주입, 필드 주입, Setter 주입 방식이 있으며, 생성자 주입이 권장됩니다.

```java
@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
```

---

### 자바 코드로 직접 스프링 빈 등록하기
1. **설정 클래스**
   - `@Configuration` 애노테이션을 사용하여 스프링 설정 클래스를 정의합니다.
   - `@Bean` 메서드로 스프링 빈을 생성하고 컨테이너에 등록합니다.

```java
@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
```

2. **코드 기반 설정의 장점**
   - **명시성**: 빈 등록과 의존성 설정이 코드로 명확하게 드러납니다.
   - **유연성**: 런타임에 따라 빈 생성 로직을 변경할 수 있습니다.

3. **생성자 주입 권장 이유**
   - 의존성이 불변하고 필수적인 경우, 생성자를 통해 주입받는 것이 적합합니다.
   - 테스트와 리팩토링이 쉬워집니다.
