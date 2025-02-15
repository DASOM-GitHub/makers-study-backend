### 회원관리 예제 웹MVC로 개발
#### 홈화면 추가방법.
1. string home을 한 다음 return home을 return하기
2. 회원가입과 회원목록을 링크로 연결하여 HTML코드를 작성한다.
#### 회원 웹기능 등록
1. 회원 이름을 등록하여 스프링으로 보낼때 POST방식 사용.
2. 등록된 이름은 스프링에서 getter setter메소드로 저장
3. 저장한 이름은 메소드를 사용하여 저장하고 호출한다.
#### 회원 웹기능 목록
1. HTML코드로 목록에 들어갈 양식을 코드로 제작한다. 
2. HTML코드에 필요한 기능을 스프링으로 할당한다.
### 스프링 DB 접근 기술
1.  H2 데이터베이스 설치( h2 데이터베이스 버전은 스프링 부트 버전에 맞춘다.
    )
2. 순수 JDBC 환경설정
#### 개방-폐쇄 원칙(OCP, Open-Closed Principle)확장에는 열려있고, 수정, 변경에는 닫혀있다.
#### 스프링의 DI (Dependencies Injection)을 사용하면 래스를 변경 할 수 있다. 기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경할 수 있다.
#### 회원을 등록하고 DB에 결과가 잘 입력되는지 확인하자.
#### 데이터를 DB에 저장하므로 스프링 서버를 다시 실행해도 데이터가 안전하게 저장된다.
### 스프링 JdbcTemplate
1. 순수 Jdbc와 동일한 환경설정을 하면 된다. 
2. 스프링 JdbcTemplate과 MyBatis 같은 라이브러리는 JDBC API에서 본 반복 코드를 대부분 제거해준다. 하지만 SQL은 직접 작성해야 한다.
###  JPA
1.  JPA는 기존의 반복 코드는 물론이고, 기본적인 SQL도 JPA가 직접 만들어서 실행해준다.
2.  JPA를 사용하면, SQL과 데이터 중심의 설계에서 객체 중심의 설계로 패러다임을 전환을 할 수 있다.
3.  JPA를 사용하면 개발 생산성을 크게 높일 수 있다.
4. 스프링은 해당 클래스의 메서드를 실행할 때 트랜잭션을 시작하고, 메서드가 정상 종료되면 트랜잭션을 커밋한다. 만약 런타임 예외가 발생하면 롤백한다.
5.  JPA를 통한 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
### 스프링 데이터 JPA
#### 스프링 부트와 JPA만 사용해도 기존에 해야할 작업 분량이 확연히 줄어들고, 생산성이 확연히 증가하나 여기에 스프링 데이터 JPA를 사용하면, 
#### 기존의 한계를 넘어 마치 마법처럼, 리포지토리에 구현 클래스 없이 인터페이스 만으로 개발을 완료할 수 있음.
#### 기본 CRUD 기능도 스프링 데이터 JPA가 모두 제공함.

### 스프링 데이터 JPA 제공 기능
1. 인터페이스를 통한 기본적인 CRUD
2. findByName() findByEmail()처럼 메서드 이름 만으로 조회 기능 제공
3. 페이징 기능 자동 제공
