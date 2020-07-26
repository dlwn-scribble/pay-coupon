# 쿠폰 시스템
 쿠폰 관리 API 

## 개발 환경
* Java 1.8 +
* Spring Boot 2.2.7
* JPA, QueryDsl
* Gradle 6.4.1

## 빌드 및 실행 방법

### Coupon API
```
cd pay-coupon-api
gradle clean bootRun
```
##### 기본 경로
* API 도큐먼트 : http://localhost:8082/
* H2 DB 접근 : http://localhost:8082/h2

### Alarm App
```
cd pay-coupon-app
gradle clean bootRun
```

## 문제 해결 전략
### 사용자 API (인증)
>API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출 시에 HTTP Header에 발급받은 토큰을 가지고 호출하세요.

#### 1. 계정 생성
> signup 계정생성 API: ID, PW를 입력 받아 내부 DB에 계정을 저장하고 토큰을 생성하여 출력한 다. 단, 패스워드는 안전한 방법으로 저장한다.
>> * 패스워드 암호화 : Spring Security BCrypt 암호화 후 저장
##### Request 
`POST /api/v1/signup?id=user1&pw=user1234`

| Parameter| 설명                                |
| -------- | -----------------------------------|
| `id`  | 아이디                        |
| `pw` | 패스워드 |

##### Response
```
{
  "tokenType": "Bearer ",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1OTU2NTk1NDQsImV4cCI6MTU5NTY2MTM0NH0.W39RvbO6U7pFPz462P9R4Lkg2pkj_NBlYle5Jhl9Sd0"
}
```
***
#### 2. 로그인 
> signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급한다.
##### Request 
`POST /api/v1/signin?id=user1&pw=user1234`

| Parameter| 설명                                |
| -------- | -----------------------------------|
| `id`  | 아이디                        |
| `pw` | 패스워드 |

##### Response
```
{
  "tokenType": "Bearer ",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1OTU2NTk2MzgsImV4cCI6MTU5NTY2MTQzOH0.Dyoz_YdbugTUhXlcD1iRa1TV3SUwHvgfFr6pTF-qM48"
}
```
![](auth.gif)
***

### 쿠폰 관리 API
#### 1. 쿠폰 생성 
> 필수 문제 1. 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현하세요.
>> * 쿠폰 유효 기간 : 쿠폰은 생성 시에는 지급 후 사용 가능한 유효 일자만 저장
>> * 쿠폰 ID : 대량 생성 시 영향이 없도록 DB 의존 없이 생성 하면 서도 유일성을 보장하기 위해 UUID 로 구현

##### Request 
`POST /api/v1/coupons?count=1&period=3`

| Parameter| 설명                                |
| -------- | -----------------------------------|
| `count`  | 생성할 쿠폰 수                         |
| `period` | 쿠폰 유효 기간 (일자), 지급 후 익일 부터 계산 |

##### Response
```
{
  "message": "1 개 쿠폰이 생성 되었습니다.",
  "couponResponseList": null
}
```
***
#### 2. 쿠폰 지급 
> 필수 문제 2. 생성된 쿠폰중 하나를 사용자에게 지급하는 API를 구현하세요. output : 쿠폰번호(XXXXX-XXXXXX-XXXXXXXX) 
>> * 쿠폰 만료 기간 : 쿠폰이 사용자에게 지급된 이후 등록된 유효기간을 기준으로 만료일자 산정. 사용자가 인지가능 하도록 Date 값으로 관리
##### Request 
`POST /api/v1/coupons/offer?userId=juyoung`

| Parameter| 설명                                |
| -------- | -----------------------------------|
| `userId`  | 지급할 사용자 아이디                       |

##### Response
```
{
  "message": "쿠폰 번호 (5c018090-3c2a-4cec-a0e8-6bf9eb179c92)",
  "couponResponseList": [
    {
      "couponId": "5c018090-3c2a-4cec-a0e8-6bf9eb179c92",
      "createdDate": "2020-07-26T22:13:52.189",
      "userId": "juyoung",
      "expireDate": "2020-07-30T00:00:00",
      "used": false
    }
  ]
}
```
***
#### 3. 쿠폰 사용
> 필수 문제 4. 지급된 쿠폰중 하나를 사용하는 API를 구현하세요. (쿠폰 재사용은 불가) input : 쿠폰번호 
>> * 사용 가능 조건 : 미사용된 쿠폰만 사용 처리 가능
##### Request 
`POST /api/v1/coupons/use?couponId=f83af60b-6494-43b2-8b67-3938e5350a66`

| Parameter| 설명                                |
| -------- | -----------------------------------|
| `couponId`| 쿠폰 아이디                    |

##### Response
```
{
  "message": "쿠폰 (f83af60b-6494-43b2-8b67-3938e5350a66) 사용 처리 되었습니다.",
  "couponResponseList": [
    {
      "couponId": "f83af60b-6494-43b2-8b67-3938e5350a66",
      "createdDate": "2020-01-01T19:05:41.885",
      "userId": "juyoung",
      "expireDate": "2020-08-02T00:00:00",
      "used": true
    }
  ]
}
```
***
#### 4. 쿠폰 사용 취소
> 필수 문제 5. 지급된 쿠폰중 하나를 사용 취소하는 API를 구현하세요. (취소된 쿠폰 재사용 가능) input : 쿠폰번호
>> * 취소 가능 조건 : 사용된 쿠폰만 취소 처리 가능
##### Request 
`POST /api/v1/coupons/cancel?couponId=f83af60b-6494-43b2-8b67-3938e5350a66`

| Parameter| 설명                                |
| -------- | -----------------------------------|
| `couponId`  | 쿠폰 아이디                   |

##### Response
```
{
  "message": "쿠폰 (f83af60b-6494-43b2-8b67-3938e5350a66) 사용 취소 처리 되었습니다.",
  "couponResponseList": [
    {
      "couponId": "f83af60b-6494-43b2-8b67-3938e5350a66",
      "createdDate": "2020-01-01T19:05:41.885",
      "userId": "juyoung",
      "expireDate": "2020-08-02T00:00:00",
      "used": false
    }
  ]
}
```
***
#### 5. 쿠폰 조회
> 필수 문제 3. 사용자에게 지급된 쿠폰을 조회하는 API를 구현하세요.
>> * 지급된 쿠폰 : 사용자 번호를 가진 쿠폰만 전달
>> * 사용자 정보 : 사용자 정보는 별도 테이블로 관리하되 쿠폰 정보에는 사용자 번호로 지급 관계 생성. 
##### Request 
`POST /api/v1/coupons?offered=true`

| Parameter| 설명                                |
| -------- | -----------------------------------|
| `offered`  | 지급 여부                     |

##### Response
```
{
  "couponResponseList": [
    {
      "couponId": "f83af60b-6494-43b2-8b67-3938e5350a66",
      "createdDate": "2020-01-01T19:05:41.885",
      "userId": "juyoung",
      "expireDate": "2020-08-02T00:00:00",
      "used": false
    },
    {
      "couponId": "70a9ea66-ad57-4970-b3bf-b9f011fd7d3c",
      "createdDate": "2020-01-01T19:05:41.885",
      "userId": "juyoung",
      "expireDate": "2020-07-31T00:00:00",
      "used": false
    }
  ]
}
```
> 필수 문제 6. 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회하는 API를 구현하세요.
>> * 만료 일자 : 만료 일자를 조회 파라미터로 받아 만료 예정 등의 정보도 조회 가능 하도록 처리
##### Request 
`POST /api/v1/coupons?expireDate=2020-08-02&offered=true`

| Parameter| 설명                                |
| -------- | -----------------------------------|
| `expireDate`  | 만료 일자 (오늘 일자)                    |
| `offered`  | 지급 여부 (지급)                    |

##### Response
```
{
  "couponResponseList": [
    {
      "couponId": "f83af60b-6494-43b2-8b67-3938e5350a66",
      "createdDate": "2020-01-01T19:05:41.885",
      "userId": "juyoung",
      "expireDate": "2020-08-02T00:00:00",
      "used": false
    }
  ]
}
```
### API (공통) 
##### Error Response
```
{
  "errorCode": "C005",
  "message": "이미 사용된 쿠폰입니다."
}
```

### 쿠폰 알람 App
> 선택 문제 7. 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송하는 기능을 구현하 세요. (실제 메세지를 발송하는것이 아닌 stdout 등으로 출력하시면 됩니다.)
>> * Spring Boot Batch 기반 구현  
>> PagingItemReader 사용 하되 대량 데이터 처리 중 중단 될 경우를 고려 하여 
>> 1. 1회 데이터 처리량을 제어
>> 2. 재발송을 방지 하기 위해 처리 발송 이력을 DB 에 저장 
>> 3. Page Index 1로 고정
```
>>>[만료 예정 쿠폰] 3 개 데이터
>>>[쿠폰 만료 알람] UserID : juyoung, Coupon ID (fc75229a-7504-415f-829e-aba75b912f34) 쿠폰이 3일 후 만료됩니다.
>>>[쿠폰 만료 알람] UserID : juyoung, Coupon ID (f89ee8b9-7a58-4cd4-8d6f-5f3ed076f655) 쿠폰이 3일 후 만료됩니다.
>>>[만료 예정 쿠폰] 1 개 데이터
>>>[쿠폰 만료 알람] 사용자 정보가 없습니다., Coupon ID be4a3904-2c9b-4139-82c5-e67738569c41
```