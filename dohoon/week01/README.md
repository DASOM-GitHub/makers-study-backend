## 1주차 - 스프링 웹 개발 기초

###정적 컨텐츠
스프링 부트는 url로 요청이 들어오면(에:localhost:8080/hello-static.html) <br>
해당 요청 관련 컨트롤러가 없다면 바로 resources/static/폴더에 있는 html파일을 반환해준다.<br>
<br>
-----------------
###MVC와 템플릿 엔진
MVC : Model, View, Controller <br>
정적 컨텐츠와 달리 해당 요청 관련 컨트롤러가 있는 경우 <br>
컨트롤러를 통해 반환을 하고 viewResolver가 templates폴더에 있는 파일을 html로 변환후 반환해준다. <br>
<br>
-----------------
###API
@ResponseBody를 사용하면 viewResolver를 사용하지 않음<br>
대신에 HTTP의 Body에 문자 내용을 직접 반환<br>
viewResolver 대신에 HttpMessageConverter가 동작<br>
기본 문자 처리 : StringHttpMessageConverter<br>
```
@Controller
public class HelloController {
 @GetMapping("hello-string")
 @ResponseBody
 public String helloString(@RequestParam("name") String name) {
 return "hello " + name;
 }
}
```
기본 객체 처리 : JSON<br>
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


