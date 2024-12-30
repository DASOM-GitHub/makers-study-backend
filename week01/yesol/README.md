# 1~3섹션

- 스프링 프로젝트 생성 ⇒ [https://start.spring.io](https://start.spring.io/)
    - **Project**  :  Gradle - Groovy project
    - **Language**  :  Java
    - **GroupId**  :  hello
    - **ArtifactId**  :  hello-spring
    - **Dependencies**  :  Spring Web, Thymeleaf
    - generate → hello-spring.zip 압축 해제 → intellij - open or import → build.gradle 열기
        - gradle ⇒ 버전 설정, 라이브러리 가져와 줌
    
    ---
    
- 실행하기
    - src → main → java → HelloController 실행
- welcome page 기능
    - src → main → resources → static → index.html
- thymeleaf 탬플릿 엔진 동작 확인
    - src → main → java → 패키지 생성 (hello.hellospring.controller) → 클래스 생성 (HelloController)
    - @Controller → import하기
        
        ```java
         @GetMapping("hello")
         public String hello(Model model) {
             model.addAttribute("data", "Hello!!");
             return "hello";
         }
        ```
        
    - src → main → resources → templates → 파일 생성(hello.html)
        
        ```html
        <!DOCTYPE HTML>
        <html xmlns:th="http://www.thymeleaf.org">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport"
                  content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
            <meta http-equiv="X-UA-Compatible" content="ie=edge">
            <title>Hello</title>
        </head>
        <body>
        <p th:text="'안녕하세요. '+ ${data}">안녕하세요. 손님</p>
        </body>
        </html>
        ```
        
    - 서버 재시작 후 [http://localhost:8080/hello](http://localhost:8080/hello)

---

- 콘솔에서 빌드, 실행
    - cmd → C:\backend_stu\hello-spring\hello-spring 이동 → gradlew build ⇒ build 폴더 생김
    - build/libs 이동 → java -jar ~~.jar 실행 →  [http://localhost:8080/hello](http://localhost:8080/hello)
    - gradlew clean ⇒ build 폴더 삭제됨

---

- 정적 컨텐츠
    - src → main → resources → static → 파일 생성 (hello-static.html)
        
        ```html
         <!DOCTYPE HTML>
         <html>
         <head>
            <title>static content</title>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
         </head>
         <body>
        정적 컨텐츠 입니다.
         </body>
         </html>
        ```
        
    - [http://localhost:8080/hello-static.html](http://localhost:8080/hello-static.html) 
    ⇒ 그대로 보여줌(프로그래밍 X)

---

- MVC, 템플릿 엔진
    - MVC ⇒ Model / View / Controller

- <HelloController.java> → 추가
    
    ```java
    @GetMapping("hello-mvc")
    public String hellomvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }
    ```
    
- src → main → resources → templates → 파일 생성(hello-template.html)
    
    ```html
     <html xmlns:th="http://www.thymeleaf.org">
     <body>
     <p th:text="'hello ' + ${name}">hello! empty</p>
     </body>
     </html>
    ```
    
- [localhost:8080/hello-mvc](http://localhost:8080/hello-mvc) ⇒ error
- [localhost:8080/hello-mvc?name=spring!!!](http://localhost:8080/hello-mvc?name=spring!!!) 
⇒ 동작 (viewResolver를 통해 html이 출력)

---

- API
    - <HelloController.java> → 추가
        
        ```java
        @GetMapping("hello-string")
        @ResponseBody // http 바디부분에 직접 넣겠다. HttpMessageConverter 동작
        public String hellostring(@RequestParam("name") String name) {
            return "hello " + name;
        }
        ```
        
    - [localhost:8080/hello-string?name=spring!!!!!!](http://localhost:8080/hello-string?name=spring!!!!!!) 
    ⇒ 동작 (문자 자체를 그대로 출력)

- <HelloController.java> → 추가
    
    ```java
     @GetMapping("hello-api")
     @ResponseBody
     public Hello helloApi(@RequestParam("name") String name) {
         Hello hello = new Hello();
         hello.setName(name);
         return hello;
     }
     
     static class Hello {
         private String name;
    
         public String getName() {
             return name;
         }
         public void setName(String name) {
             this.name = name;
         }
     }
    ```
    
    - [localhost:8080/hello-api?name=spring!!!!!!](http://localhost:8080/hello-api?name=spring!!!!!!) 
    ⇒ JSON 형태로 출력 { ”name” : “spring!!!” }