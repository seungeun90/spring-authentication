# spring-authentication
oauth2-client, email-authentication, JWT , etc..

가입하는 방식 
1. kakao authentication
2. pass authentication & email authentication

가입 시 JWT 발행으로 자동 로그인 처리 

- 각종 인증 처리를 별도의 모듈로 분리 
- email 인증 코드 , jwt 관리는 Redis
- 회원은 RDBMS
