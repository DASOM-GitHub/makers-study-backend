# 1주차 -섹션 3. 스프링 웹 개발 기초
<br>
---

### 정적 컨텐츠
서버에서 HTML파일을 그대로 웹브라우저에 그대로 내려주는 것.<br>

### MVC와 템플릿 엔진
<br>
MCV: Model, View, Controller<br>
정적 컨텐츠와 템플릿 엔진-MCV의 차이<br>
: 정적 컨텐츠는 파일을 그대로 가져오지만, <br>
템플릿 엔진과 MVC는 서버에서 프로그래밍을 통해 정적 컨텐츠를 동적으로 바꿔 내려준다.<br>

### API
데이터를 직접 전달하는 방식. <br>
JSON이라는 데이터 구조 포멧으로 클라이언트에게 데이터를 전달한다.<br>
서버끼리 통신을 할 떄 사용한다. <br>

```
@Controller
public class HelloController {

    @GetMapping
    public String hello (Model model) {
        model.addAttribute("data", "반가워요?!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model){
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

}
```

#### @ResponseBody
http는 header부, body부가 존재하는데, <br>
@ResponseBody는 body부에 데이터를 직접 넣겠다는 것이다.<br>


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




