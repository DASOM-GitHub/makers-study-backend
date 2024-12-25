1주차<br>
스프링 웹 개발 기초<br>

- 정적 컨텐츠<br>
파일을 그대로 웹 브라우저에 전달<br>
프로그래밍을 할 수는 없음<br>

- MVC와 템플릿 엔진<br>
템플렛 엔진: html을 동적으로 바꿈<br>
MVC: Model, View, Controller<br>
Html을 변환하여 웹브라우저에 전달<br>
View는 화면과 관련된 일만, 그 외 일은 controller이 처리, 필요한 것들을 담다 화면에 넘기는 게 model<br>
웹 브라우저->내장 톰켓 서버->helloController->viewResolver(Thymeleaf 템플릿 엔진 처리)->HTML 변환 후 웹 브라우저에 전달<br>

- API<br>
JSON 이라는 데이터 구조 포맷으로 클라이언트에게 데이터 전달하는 방식<br>
@ResponseBody : HTML의 응답 바디에 이 데이터를 직접 넣겠다는 것<br>
템플릿 엔진과의 차이: view 없이 문자 그대로 웹 브라우저에 전달<br>
@ResponseBody를 사용할 시<br>
  - HTTP의 BODY에 문자 내용을 직접 반환<br>
  - viewResolver 대신에 httpMessageConverter가 동작<br>
  - 기본 문자처리: StringHttpMessageConverter<br>
  - 기본 객체처리: MappingJackson2HttpMessageConverter (객체를 json으로 바꾸어 주는 라이브러리가 Jackson)<br>
  - byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음<br>

