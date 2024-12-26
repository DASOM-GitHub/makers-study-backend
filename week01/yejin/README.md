# 1주차 - 스프링 웹 개발 기초

### 정적 컨텐츠
###### 서버에서 어떠한 동작을 하는 것이 아닌 존재하는 html 파일만 그대로 내여주는  작업을 의미한다.

### MVC와 템플릿 엔진

###### MCV: Model, View, Controller<br>
###### Model -  어플리케이션의 데이터를 의미하며 사용자 인터페이스(뷰)와 독립적이다. 
###### View - 사용자 인터페이스 요소를 나타낸다. (사용자가 보게되는 모습)
###### Controller - 사용자의 요청을 받아들이고 그것을 해석하여 모델이나 뷰에 명령한다.
###### 정적 컨텐츠와의 차이 : 정적 컨텐츠는 파일을 그대로 가지고 오지만, MVC와 템플릿 엔진은 프로그래밍을 통해 동적으로 바꿔서 응답한다.

### API
###### 데이터를 바로 내리는 방식
###### JSON이라는 데이터 구조 포멧을 사용한다.
###### @ResponseBody를 사용하여 문자 내용을 직접 반환한다.
###### 객체처리 - JsonConverter / 문자처리 - StringConverter



```
@Controller
public class HelloController {

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello{
        private String name; // 키

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
```