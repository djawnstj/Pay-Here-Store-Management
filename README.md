
## 프로젝트 요약
### 구현
 - Spring Security 와 Jjwt 를 이용해 JWT 인증/인가를 구현하였습니다.
 - 토큰 재발급 시 `accessToken` 이 아직 유효하다면 `refreshToken` 이 탈취됐다 판단해 저장된 토큰을 삭제하도록 구현하였습니다.
 - Spring Security 의 `BcryptPasswordEncoder` 를 이용해 단방향 암호화로 비밀번호를 안전하게 저장할 수 있도록 하였습니다.
 - 초성을 이용한 상품 검색 기능은 ElasticSearch 를 이용해 상품을 등록/수정할 때 상품 이름에서 한글 초성을 추출해 인덱스로 저장하여 구현하였습니다.
 - 토큰 발급 시 토큰을 저장하는 저장소는 mysql 로 1차적으로 구현하였습니다. 추후에 다른 저장소로 바뀌게 되더라도 호출 코드에서 영향이 없도록 interface 로 분리, Spring Bean 을 사용하여 수평확장에 용이하도록 구현하였습니다.
   - 성능상 비교적 RDB 보다 비용이 적고 빠른 Redis 를 이용해 토큰을 저장하도록 수정하였습니다. 기대했던대로 호출하는 코드에선 수정 없이 기능이 변경되는 `SOLID` 원칙의 `OCP` 가 어느정도 지켜졌다고 판단하였습니다.
 - DB DDL 은 `/docker/database/init` 경로에 만들어두었습니다.
 - 도커 실행은 `/.env` 파일에서 mysql `username` 과 `password`, docker `username` 을 변경 후 `docker-compose up` 명령어로 실행할 수 있게 설정하였습니다.
 - 도커 실행은 SpringBoot 빌드, mysql, ElasticSearch, Redis, SpringBoot jar 실행 을 한번에 할 수 있도록 하였습니다.

### 아쉬운 점
 - 엘라스틱 서치를 제대로 공부하지 못한채로 사용해 성능적인 효율성을 고려하지 못하였습니다. 
 - 도커 또한 간단하게 사용해본 경험으로 프로젝트에 적용하려 하다 보니 기능 구현에 급급했던거 같습니다.
 - 도커와 엘라스틱 서치에 대해 공부하고자 하는 계기가 됐습니다.
 - 현재 도커를 이용해 서버를 실행시키면 초기화까지 시간이 많이 소요됩니다. 이는 치명적인 성능상 이슈라고 생각되고 꾸준히 학습하여 최적화에도 많은 기여를 하고자 합니다.

## API
### 회원가입
- URL: `/api/v1/members`
- Method: `POST`
- Request
```json
{
  "phoneNumber": "010-0000-0000",
  "loginPassword": "password",
  "name": "name",
  "role": "OWNER"
}
```

<br>

### 로그인
- URL: `/api/v1/log-in`
- Method: `POST`
- Request
```json
{
  "phoneNumber": "010-0000-0000",
  "loginPassword": "password"
}
```
- Response
```json
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "accessToken": "..",
    "refreshToken": ".."
  }
}
```

<br>

### 토큰 재발급
- URL: `/api/v1/auth/refresh`
- Method: `POST`
- Header
``` json
Authorization: Bearer {accessToken}
```
- Request
```json
{
  "accessToken": "accessToken",
  "refreshToken": "refreshToken"
}
```
- Response
```json
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "accessToken": "..",
    "refreshToken": ".."
  }
}
```

<br>

### 로그아웃
- URL: `/api/v1/auth/logout`
- Method: `POST`
- Header
``` json
Authorization: Bearer {accessToken}
```

<br>

### Refresh Token
- URL: `/api/v1/auth/refresh-token`
- Method: `POST`
- Request
```json
{
  "refreshToken": ".."
}
```
- Response
```json
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "accessToken": "..",
    "refreshToken": ".."
  }
}
```

<br>

### 상품 카테고리 등록
- URL: `/api/v1/products`
- Method: `POST`
- Header
``` json
Authorization: Bearer {accessToken}
```
- Request
```json
{
  "categoryName": "커피"
}
```

<br>

### 상품 등록
- URL: `/api/v1/products`
- Method: `POST`
- Header
``` json
  Authorization: Bearer {accessToken}
```
- Request
```json
{
  "categoryId":  1,
  "sellingPrice": "4000",
  "costPrice":  "1000",
  "productName": "아메리카노",
  "description":  "아메리카노",
  "barcode": "바코드",
  "expirationDate":  "2024-03-31",
  "size": "SMALL"
}
```

<br>

### 상품 수정
- URL: `/api/v1/products`
- Method: `PATCH`
- Header:
``` json
Authorization: Bearer {accessToken}
```
- Request
``` json
{
  "id": 1,
  "categoryId":  1,
  "sellingPrice": "3500",
  "description":  "고급 원두로 만든 아메리카노",
  "expirationDate":  "2024-03-01",
  "size": "LARGE"
}
```

<br>

### 상품 목록(페이지 네이션)
- URL: `/api/v1/products`
- Method: `GET`
- Header
``` json
Authorization: Bearer {accessToken}
```
- Query Parameter
``` json
{
  "page": 1,
  "size": 10
}
```
- Response
```json
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "products": [
      
    ]
  }
}
```
<br>

### 상품 상세
- URL: `/api/v1/products/{id}`
- Method: `GET`
- Header
``` json
Authorization: Bearer {accessToken}
```
- Response
```json
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "product": {
      
    }
  }
}
```

<br>

### 상품 검색
- URL: `/api/v1/products/search`
- Method: `GET`
- Header
``` json
Authorization: Bearer {accessToken}
```
- Query Parameter
```json
{
  "query": "ㅁㄹ"
}
```
- Response
```json
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "product": {
      
    }
  }
}
```

<br>

### 상품 삭제
- URL: `/api/v1/products`
- Method: `DELETE`
- Header
``` json
Authorization: Bearer {accessToken}
```
- Request
```json
{
  "id": 1
}
```