# 회원 도메인 개발 과제

## 1. 아키텍처 설명
프로젝트는 ably-homework 내에 아래의 두가지 모듈을 따로 개발하였습니다.
```
ably-homework
 ㄴ sms-server
 ㄴ user-api
```
+ sms-server 의 경우 시스템 사용자의 휴대폰 정보를 입력빋으면 인증번호를 생성하여 전달하는 역할을 수행합니다.
다만, 이번 프로젝트의 경우 인증번호를 생성해주는 외부 api와 연동하지 않고, 임시번호를 전달해줍니다.
  + 휴대폰 인증번호 생성 API
+ user-api 는 회원가입, 로그인, 회원 정보 조회, 비밀번호 재설정 과 같은 회원 관련 기능을 위한 api를 제공하는 어플리케이션 입니다. 
  + 휴대폰 인증번호 요청 API
  + 휴대폰 인증 API
  + 회원가입 API
  + 비밀번호 찾기 (재설정) API
  + 로그인 API
  + 내 정보 보기 API

## 2. API 명세서
### 사전 준비 작업
+ 각 모듈 내 어플리케이션을 실행합니다. (SmsServerApplication, UserApiApplication)
+ API 를 호출 할 수 있는 환경을 준비합니다. (Post Man, Swagger , Curl 등 )
### sms-server
##### 휴대폰 인증번호 생성 API
+ request url : http://127.0.0.1:15001/auth/request
+ method : POST
+ request body:
  ```
  {
      "phoneNo": "01012345678"
  }
  ```
+ response body:
  ```
  {
      "authNo": "123456",
      "phoneNo": "01012345678"
  }
  ```
+ desc : 이 API 는 user-api 에서 사용하는 api 입니다. 사용자가 직접 접근하지 못하도록 별도의 처리가 필요합니다.
### user-api
##### 휴대폰 인증번호 요청 API
+ request url : http://127.0.0.1:9009/auth/request
+ method : POST
+ request body:
  ```
  {
      "phoneNo": "01012345678"
  }
  ```
+ response body:
  ```
  {
      "authNo": "123456",
      "phoneNo": "01012345678"
  }
  ```
+ desc : 휴대폰 문자를 대신하는 기능입니다. sms-server 에서 생성된 인증번호를 전달 받을 수 있습니다.
##### 휴대폰 인증 API
+ request url : http://127.0.0.1:9009/auth
+ method : POST
+ request body 예시:
  ```
  {
      "phoneNo": "01012345678",
      "authNo": "123456"
  }
  ```
+ response body 예시:
  ```
  {
      "authNo": "123456",
      "phoneNo": "01012345678",
      "authkey": "eyJhbGciOiJIU..."
  }
  ```
+ desc : 휴대폰 인증 요청 API 를 통해 전달 받은 인증번호와 휴대폰 번호를 통해 회원가입 및 비밀번호 재설정이 가능한 인증키를 발급합니다.

##### 회원가입 API
+ request url : http://127.0.0.1:9009/join
+ method : POST
+ request header 예시
  ```
  authKey : {휴대폰 인증 API를 통해 발급된 authKey 입력}
  ```
+ request body 예시:
  ```
  {
      "email" : "abcde@naver.com",
      "nickName" : "블리블리",
      "pwd" : "a12345",
      "name" : "홍길동",
      "phoneNo": "01012345678"
  }
  ```
+ response body 예시:
  ```
  {
      "email": "abcde@naver.com",
      "name": "홍길동",
      "phoneNo": "01012345678",
      "nickName": "블리블리"
  }
  ```
+ desc : 회원 가입을 위한 API 입니다. 회원 가입은 휴대폰 인증을 통해 발급된 auth key 가 있어야 가능합니다. 
  입력된 비밀번호의 경우 내부에서 암호화하여 저장합니다.
  회원가입 시 userId 라는 값이 회원 마다 생성됩니다. 이 값은 로그인 후 사용할 서비스 (현재는 회원조회 서비스 밖에 없음)를 사용하기 위한 인증키를 만드는데 사용됩니다.
##### 비밀번호 찾기 (재설정) API
+ request url : 127.0.0.1:9009/set/password
+ method : POST
+ request header 예시
  ```
  authKey : {휴대폰 인증 API를 통해 발급된 authKey 입력}
  ```
+ request body 예시:
  ```
  {
    "email" : "abcde@naver.com",
    "pwd" : "b12345"
  }
  ```
+ response body 예시:
  ```
  {
    "email": "abcde@naver.com",
    "name": "홍길동",
    "phoneNo": "01012345678",
    "nickName": "블리블리"
  }
  ```
+ desc : 비밀번호 재설정을 위한 API 입니다. 회원 가입은 휴대폰 인증을 통해 발급된 auth key 가 있어야 가능합니다.
  변경된 비밀번호의 경우 내부에서 암호화하여 저장합니다.
##### 로그인 API
+ request url : http://127.0.0.1:9009/login
+ method : POST
+ request body 예시:
  ```
  {
    "id" : "abcde@naver.com",
    "password" : "b12345"
  }
  ```
+ response header 예시:
  ```
  token : eyJhbGciOiJIUzUxMiJ9...
  userId : c00f8950-ddc6-4750-bff5-f6674342fc10
  ```
+ desc : 로그인을 위한 API 입니다. 로그인은 이메일 or 휴대폰 번호 + 비밀번호를 통해 가능합니다. 로그인에 성공하면 회원 가입시 개별로 생성된
  userId 와 그 값을 바탕으로 만들어진 토큰이 발급됩니다. 각 서비스는 이 토큰을 사용하여 서비를 사용할 수 있습니다.

##### 내 정보 보기 API
+ request url : http://127.0.0.1:9009/users/{email}
+ method : GET
+ request header authorization 예시
  ```
  Authorization : Bearer {로그인 시 생성된 토큰 입력}
  ```
+ response body 예시:
  ```
  {
    "email": "abcde@naver.com",
    "name": "홍길동",
    "phoneNo": "01012345678",
    "nickName": "블리블리"
  }
  ```
+ desc : 회원 정보 조회를 위한 API 입니다. 사용을 위해서는 로그인 후 발급된 토큰 값이 필요합니다.
## 3. 프로젝트 설명
+ 기술 구현 스펙
  + 언어 : Java 8
  + 프레임워크 : Spring Boot 2.7.9 (Spring Security, Spring JPA 등 활용)
  + DB : H2 내장 메모리 DB
+ 추가 설명
  + 프로젝트의 아키텍처를 고려할 때, 실제 사용환경을 고려하여 휴대폰 인증을 위한 서버와 회원 서비스를 분리하여 개발하였습니다. 각 서버의 통신은 RestTemplate 
  을 사용하였습니다.
  + 로그인 및 인증을 구현하기 위하여 UsernamePasswordAuthenticationFilter를 활용하였습니다.토큰은 JWT로 발행하였습니다.
  + 전체적인 구조는 Spring MVC 의 기본 구성을 갖추었으며, 각종 설정 값은 application.yml 에 담아 제공하고 있습니다.
## 4. 요구사항에 대한 구현 범위
+ 100% 구현 완료