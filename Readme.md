# Demo

- 온라인 교육을 위한 실습생에게 제공되는 프로젝트입니다.
- Spring Boot 기반의 RESTful API 서버이며, 단순 로그인 / 권한 처리를 위한 Base 프로젝트입니다.

# Getting Started

### DB 초기 설정

- resources/sql 폴더 내의 `schema.sql` 파일을 이용하여 DB를 생성합니다.
- resources/sql 폴더 내의 `data.sql` 파일을 이용하여 초기 데이터를 입력합니다.
- application-local.yml 파일을 수정하여 DB 접속 정보를 수정합니다.

### 프로젝트 실행

- .run 폴더의 DemoApplication.run.xml 파일을 사용하여 intellij에서 프로젝트를 실행합니다.
- `http://localhost:8080` 으로 접속하여 서비스를 이용할 수 있습니다.

```Bash
# Java VM 환경 변수
-server -Dspring.profiles.active=local,mariadb 
```

### 프로젝트 빌드

- 프로젝트 루트 디렉토리에서 `./gradlew bootjar` 명령어를 실행하여 프로젝트를 빌드합니다.
- 빌드된 jar 파일은 `build/libs` 디렉토리에 생성됩니다.
- 빌드된 jar 파일을 실행하면 서버가 실행됩니다.

```Bash
java.exe -server -D'spring.profiles.active=local,mariadb' -jar .\demo.jar  
```

### Swagger

- `http://localhost:8080/swagger-ui/index.html` 으로 접속하여 API 명세를 확인할 수 있습니다.