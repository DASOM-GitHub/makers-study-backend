## 3주차 - 회원 관리 예제(웹 MVC 개발)

### **[홈화면]**
<pre>
<code>
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
  <div>
    <h1>Hello Spring</h1> <p>회원 기능</p>
    <p>
      <a href="/members/new">회원 가입</a>
      <a href="/members">회원 목록</a>
    </p>
  </div>
</div> <!-- /container -->
</body>
</html>
</code>
</pre>
### : 회원 가입과 회원 목록 중 선택해 해당하는 창으로 넘어감

* * *

### **[등록]**
<pre>
<code>
@Controller
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }
}
</code>
</pre>
### : 기존에 작성해둔 MemberController에 @GetMapping 추가해 createMemberForm 반환    

->
<pre>
<code>
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
  <form action="/members/new" method="post">
    <div class="form-group">
      <label for="name">이름</label>
      <input type="text" id="name" name="name" placeholder="이름을 입력하세요">
    </div>
    <button type="submit">등록</button> </form>
</div> <!-- /container -->
</body>
</html>
</code>
</pre>
### : input 박스에 이름을 입력해 등록 버튼을 누르면 등록

<pre>
<code>
@PostMapping(value = "/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }
</code>
</pre>
### : 회원 컨트롤러에서 회원을 실제로 등록하는 역할
### @PostMapping : 데이터를 폼에 넣어서 전달할 때 사용
- 데이터를 등록할 때 -> PostMapping
- 조회할 때 -> GetMapping

* * *

### **[조회]**
<pre>
<code>
@GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
</code>
</pre>
### : 등록된 모든 멤버를 꺼내서 리스트에 저장하고 그 리스트를 모델에 담아서 넘김    

->
<pre>
<code>
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
  <div>
    <table>
      <thead>
      <tr>
        <th>#</th>
        <th>이름</th> </tr>
      </thead>
      <tbody>
      <tr th:each="member : ${members}">
        <td th:text="${member.id}"></td>
        <td th:text="${member.name}"></td>
      </tr>
      </tbody>
    </table>
  </div>
</div> <!-- /container -->
</body> 
</code>
</pre>
### : 루프가 돌면서 객체를 하나씩 꺼내 멤버에 담고 아이디와 이름 출력

## 3주차 - 스프링 DB 접근 기술

<pre>
<code>
import hello.hello_spring.repository.JdbcMemberRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import hello.hello_spring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        // return new MemoryMemberRepository();
        return new JdbcMemberRepository(dataSource);
    }
}
</code>
</pre>
### : 기존 메모리 버전의 멤버 리포지토리를 빼고 jdbc 버전의 멤버 리토지토리를 등록하면 구현체만 바꿔줌
### 개방-폐쇄 원칙(OCP, Open-Closed Principle) : 확장에는 열려있고, 수정, 변경에는 닫혀있다
### 스프링의 DI(Dependencies Injection)를 사용하면 기존 코드에 손대지 않고, 설정 수정만으로 구현 클래스 변경 가능

<pre>
<code>
@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

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
    }
}
</code>
</pre>
### **@SpringBootTest** : 스프링 컨테이너와 테스트를 함께 실행
### **@Transactional** : 테스트 시작 전에 트랜잭션을 시작하고, 테스트 완료 후에 항상 롤백. DB에 데이터가 남지 않아 다음 테스트에 영향 X

### **[Jdbc]**
<pre>
<code>
import hello.hello_spring.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
</code>
</pre>

<pre>
<code>
@Bean
    public MemberRepository memberRepository() {
        return new JdbcTemplateMemberRepository(dataSource);
    }
</code>
</pre>
### : Jdbc를 Jdbc 템플릿으로 변경
- Jdbc와 동일한 환경설정
- JDBC API에서 본 반복 코드를 대부분 제거
- SQL 직접 작성

### **[JPA]**
- 반복 코드와 SQL 모두 JPA가 직접 만들어 실행
- SQL과 데이터 중심의 설계에서 객체 중심의 설계로 패러다임을 전환 가능

### **[스프링 데이터 JPA]**
- 스프링 부트와 JPA만으로도 개발 생산성 증가
- 스프링 데이터 JPA를 사용하면 리포지토리에 구현 클래스 없이 인터페이스 만으로 개발 가능
- 반복 개발해온 기본 CRUD 기능도 스프링 데이터 JPA가 모두 제공
- 단순하고 반복적 개발 코드들이 줄어들어 개발자가 핵심 비즈니스 로직을 개발하는데 집중할 수 있음
- 제공 기능
    - 인터페이스를 통한 기본적인 CRUD
    - 메서드 이름 만으로 조회 기능 제공 (findByName(),findByEmail() 등)
    - 페이징 기능 자동 제공
