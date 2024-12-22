## 1주차 - 섹션 3 : 스프링 웹 개발 기초

### **1. MVC(Model-View-Controller) 모델**

- MVC 모델은 애플리케이션의 구성 요소를 **Model**, **View**, **Controller**로 분리하여 개발하는 방식.
    - **Model**: 데이터와 비즈니스 로직을 처리.
    - **View**: 사용자에게 보여지는 화면.
    - **Controller**: 사용자의 요청을 받아 Model과 View를 연결.

---

### **2. ResponseBody**

- `@ResponseBody`는 메서드의 반환값을 **HTTP Response의 Body**에 직접 전달.
- HTML 등의 View 템플릿을 사용하지 않고, 데이터를 바로 전달.
    - 문자열, JSON 객체 등을 그대로 반환.

---

### **3. API 방식**

- API 방식은 주로 **JSON 객체**를 사용하여 데이터 통신을 수행.
    - 서버가 클라이언트와 데이터를 주고받는 데 사용.
    - RESTful API는 표준적인 URL 패턴과 HTTP 메서드를 통해 데이터 송수신.

---

### **4. 코드 예제**

### **4.1 HelloController (Spring Controller 예제)**

```java
package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    // 기본 hello 메서드 (문자열 반환)
    @GetMapping
    @ResponseBody
    public String helloController() {
        return "hello";
    }

    // MVC 방식 (Model + View)
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name); // Model에 데이터 추가
        return "hello-template"; // View 템플릿 이름 반환
    }

    // ResponseBody로 단순 문자열 반환
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    // JSON 객체 반환
    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name); // 객체에 데이터 설정
        return hello; // JSON 형태로 반환
    }

    // 내부 static class (Hello DTO)
    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
```

### **4.2 View 파일**

1. **Thymeleaf 템플릿 파일 (hello-template.html)**
    - 서버에서 전달된 데이터를 HTML로 렌더링.
    
    ```html
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org" lang="en">
    <body>
    <p th:text="'hello ' + ${name}">hello! empty</p>
    <!-- hello empty: 서버 없이 URL 직접 접근 시 표시되는 기본값 -->
    <!-- 주로 HTML 파일을 단독으로 확인할 때 유용 -->
    </body>
    </html>
    ```
    
2. **정적 콘텐츠 (static HTML 파일)**
    - 서버 처리가 필요 없는 정적인 HTML 파일.
    
    ```html
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
    정적 컨텐츠입니다.
    </body>
    </html>
    ```
    

---

### **5. 정리 :**

1. **MVC 방식**
    - `hello-mvc` URL 호출 시:
        1. Controller가 요청을 받아 `Model`에 데이터를 담음.
        2. View 템플릿(Thymeleaf)에서 `Model`의 데이터를 렌더링.
        3. 최종 HTML이 사용자에게 반환.
2. **ResponseBody 방식**
    - `hello-string` 또는 `hello-api` 호출 시:
        1. 데이터를 가공한 문자열이나 객체를 반환.
        2. JSON 변환을 통해 클라이언트에게 전달.

---
