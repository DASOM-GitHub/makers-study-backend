## 3주차 - 섹션 6, 7 : 회원 관리 예제 - 웹 MVC 개발 & 스프링 DB 접근 기술
---

### URL 매핑
- `@PostMapping`: **데이터 전송**을 처리할 때 사용.
  - 예) 회원가입 (`members/new`)에서 데이터를 서버로 전송.
- `@GetMapping`: **데이터 조회**를 처리할 때 사용.
  - 예) 회원목록 조회 (`members`) 페이지.

URL은 같더라도 요청 방식(`POST`, `GET`)에 따라 처리 방식이 다르므로, 각각의 매핑 어노테이션을 통해 구분.

---

### JPA란?
- **JPA(Java Persistence API)**: Java에서 데이터베이스 작업을 객체지향적으로 처리하기 위한 **표준 인터페이스**.
- Hibernate는 JPA의 구현체로, 스프링 JPA에서 주로 사용.
- 주요 특징:
  1. **ORM(Object-Relational Mapping)** 기술 사용:
     - 객체와 데이터베이스 테이블을 자동으로 매핑.
     - SQL 대신 메서드 호출로 데이터베이스 작업 수행.
  2. **간단한 설정**:
     - 예) `@Entity`, `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)` 등을 통해 매핑.

#### JPA 설정
- **테이블 자동 생성 설정:**
  ```properties
  spring.jpa.hibernate.ddl-auto=none
  ```
  
- 기본적으로 JPA는 엔티티를 기반으로 테이블을 자동 생성.
- `none`으로 설정하면 테이블은 수동으로 생성.
- **엔티티 매핑:**
    
    ```java
    @Entity
    public class Member {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
    }
    ```
    
    - `@Entity`: 클래스가 JPA 엔티티임을 나타냄.
    - `@Id`: 기본 키(PK) 필드 지정.
    - `@GeneratedValue`: PK 값 자동 생성 방식 지정.

#### JPA 주요 메서드

1. **저장**:
    
    ```java
    entityManager.persist(member);
    ```
    
    - 엔티티를 데이터베이스에 저장.
2. **조회**:
    
    ```java
    Member member = entityManager.find(Member.class, id);
    ```
    
    - PK를 기준으로 엔티티 조회.
3. **주의사항: 트랜잭션 필요**
    - JPA 작업은 트랜잭션 내부에서 실행해야 데이터 무결성과 일관성을 보장.
    - `@Transactional` 어노테이션 사용.

---

### Spring JDBC

- JDBC를 편리하게 사용할 수 있도록 스프링에서 제공하는 템플릿 기반의 API.
- **특징**:
    1. SQL 작성 필요: SQL 문은 직접 작성해야 함.
    2. 객체 매핑 지원: `RowMapper`나 `BeanPropertyRowMapper`로 결과를 Java 객체에 매핑.

#### Spring JDBC 장점

- **단순하고 직관적**:
    - SQL 작성 후 메서드 호출만으로 데이터 처리 가능.
- **자동 자원 관리**:
    - 데이터베이스 연결, 자원 해제 등 반복적인 작업 자동 처리.

#### Spring JDBC 코드 예제

```java
public List<Member> getMembers() {
    String sql = "SELECT * FROM members";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Member.class));
}
```

---

### Spring Test

1. **SpringBootTest**:
    - 스프링 컨테이너와 함께 테스트 실행.
    - 통합 테스트(Integration Test)에 사용.
2. **@Transactional**:
    - 테스트 메서드에서 사용 시, 테스트가 끝난 후 데이터 롤백.
    - DB 상태를 깨끗하게 유지하여 반복 테스트 가능.

---

### 객체지향 설계 원칙: 개방-폐쇄 원칙(OCP)

- **OCP(Open-Closed Principle)**:
    - **확장에는 열려있고, 수정에는 닫혀있다**.
    - 즉, 기존 코드를 수정하지 않고도 기능을 확장 가능.

#### DI(Dependency Injection)와 OCP

- 스프링의 DI(의존성 주입)을 통해 구현 클래스 교체가 가능.
- 기존 코드를 수정하지 않고 새로운 구현체로 대체 가능.

---

### JPA와 JDBC 비교

| **항목** | **JPA** | **Spring JDBC** |
| --- | --- | --- |
| **쿼리 작성** | SQL 없이 메서드 호출로 처리 | SQL 직접 작성 필요 |
| **매핑 방식** | ORM 기반: 엔티티와 테이블 매핑 | `RowMapper`나 `BeanPropertyRowMapper` 사용 |
| **트랜잭션** | 필수 | JDBC 자체로도 가능 |
| **학습 곡선** | 상대적으로 높음 | 비교적 낮음 |
| **성능 최적화** | 복잡한 경우 튜닝 필요 | 튜닝은 SQL 기반으로 직접 가능 |

---
