# 카카오페이 뿌리기

## 개발환경
- JAVA 8
- Spring Boot 2.3.5.RELEASE
- JPA
- QueryDSL
- MySQL 8
- Gradle 6.6
- Junit5


## 빌드 및 실행
- DB
  - 기본설정 : localhost:3306/testdb
  - 아래 명시된 DDL, DML 실행
    - 테이블 (DDL)
      - SEND_MONEY
        ```
          CREATE TABLE `send_money` (
            `send_id` bigint NOT NULL AUTO_INCREMENT,
            `user_id` bigint NOT NULL,
            `room_id` char(5) NOT NULL,
            `amt_cnt` int NOT NULL,
            `token` char(3) NOT NULL,
            `send_amt` bigint NOT NULL,
            `send_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
            PRIMARY KEY (`send_id`),
            UNIQUE KEY `idx_token` (`token`)
          );
        ```
      - RECV_MONEY
        ```
        CREATE TABLE `recv_money` (
          `recv_id` bigint NOT NULL,
          `recv_seq` bigint NOT NULL,
          `each_amt` bigint NOT NULL,
          `user_id` bigint DEFAULT NULL,
          `recv_time` timestamp NULL DEFAULT NULL,
          PRIMARY KEY (`recv_id`,`recv_seq`)
        );
        ```
    - 데이터 (DML)
      - SEND_MONEY
        ```
          -- 10분 경과
          INSERT INTO `send_money` (`send_id`,`user_id`, `room_id`, `amt_cnt`, `token`, `send_amt`,`send_time`)
          VALUES (1 ,1, 'ROOMA', 3, 'SLg', 100, DATE_SUB(NOW(), INTERVAL 11 minute));
          -- 모든 받기 완료, 정상조회
          INSERT INTO `send_money` (`send_id`,`user_id`, `room_id`, `amt_cnt`, `token`, `send_amt`,`send_time`)
          VALUES (2 ,1, 'ROOMA', 2, 'Vup', 100, NOW());
          -- 조회기간 만료
          INSERT INTO `send_money` (`send_id`,`user_id`, `room_id`, `amt_cnt`, `token`, `send_amt`,`send_time`)
          VALUES (3 ,1, 'ROOMA', 3, 'nLS', 100, DATE_SUB(NOW(), INTERVAL 7 day));
        ```
      - RECV_MONEY
        ```
          -- 10분 경과
          INSERT INTO `recv_money`(`recv_id`,`recv_seq`,`each_amt`,`user_id`,`recv_time`)
          VALUES (1,1,34,2,now());
          -- 모든받기 완료,  정상조회
          INSERT INTO `recv_money`(`recv_id`,`recv_seq`,`each_amt`,`user_id`,`recv_time`)
          VALUES (2,1,50,2,now());
          INSERT INTO `recv_money`(`recv_id`,`recv_seq`,`each_amt`,`user_id`,`recv_time`)
          VALUES (2,2,50,3,now());
          -- 조회기간 만료 (7일)
          INSERT INTO `recv_money`(`recv_id`,`recv_seq`,`each_amt`,`user_id`,`recv_time`)
          VALUES (3,1,50,3,now());
        ```
  - 접속정보 변경시 해당 파일 수정이 필요
- 빌드(테스트 케이스때문에 DB생성 및 데이터가 삽입 후 즉시 실행 필요)
```
  $ git clone https://github.com/itpejylee/MoneyFly.git
  $ cd MoneyFly
  $ gradlew clean build
  $ java -jar build/libs/moneyfly-0.0.1-SNAPSHOT.jar
  
  *실행결과
  > Task :compileQuerydsl
      Note: Running JPAAnnotationProcessor
      Note: Serializing Entity types
      Note: Generating com.kakaopay.moneyfly.domain.QRecvMoney for [com.kakaopay.moneyfly.domain.RecvMoney]
      Note: Generating com.kakaopay.moneyfly.domain.QSendMoney for [com.kakaopay.moneyfly.domain.SendMoney]
      Note: Running JPAAnnotationProcessor
      Note: Running JPAAnnotationProcessor

      > Task :test
      2021-03-02 06:13:56.273  INFO 27796 --- [extShutdownHook] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
      2021-03-02 06:13:56.273  INFO 27796 --- [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
      2021-03-02 06:13:56.273  INFO 27796 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
      2021-03-02 06:13:56.273  INFO 27796 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.

      Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
      Use '--warning-mode all' to show the individual deprecation warnings.
      See https://docs.gradle.org/6.6/userguide/command_line_interface.html#sec:command_line_warnings

      BUILD SUCCESSFUL in 11s
      9 actionable tasks: 9 executed
  
```


## 구현
### 핵심 문제해결 전략
- 아키텍처 설계 : 클래스가 단일책임의 원칙을 적용하여 응집력을 높이도록 적용
- 다수의 인스턴스 : 격리단계를 REPEATABLE READ를 하여 다수의 인스턴스가 읽을때 Phantom Read 방지
- 중복없는 토큰발급 : 코드에서 중복여부를 체크, DB 컬럼을 UNIQUE INDEX로 지정하여 중복을 허용하지 않도록 조치
- 오류 : 기능상 문제가 없도록 단위 테스트 완료

### 기능설명
1. 공통
  ```
  HTTP Header : X-USER-ID (사용자 식별값, 숫자)
  HTTP Header : X-ROOM-ID (대화방 식별값, 문자)
  모든 사용자는 뿌리기 충분한 잔액 보유
  금액이 인원에 나눠서 떨어지지 않을 경우 1원씩 더 지급
  ```
  - 제약조건
    - X-USER-ID : 양수
    - X-ROOM-ID : 5자리 고정 ex)ROOMA, ROOMB .... 
  - 오류
    - K001 : 잘못된 요청정보
    ```
      {
        "timestamp": "2021-03-02T06:15:41.856",
        "status": 405,
        "code": "K001",
        "message": "Request method 'PATCH' not supported"
      }
      
    ```
    - K002 : 잘못된 파라메터 정보
    ```
    {
      "timestamp": "2021-03-02T06:15:07.669",
      "status": 400,
      "code": "K002",
      "message": "sendMoney.userId: 0보다 커야 합니다"
    }
    ```
  
2. 뿌리기 API
  - 제약조건
    -  sendAmt : 양수
    -  sendCnt : 양수
  - 요청
    ```
    POST /sendMoney
    X-USER-ID: ${userId}
    X-ROOM-ID: ${roomId}

    {
        "sendAmt": 100,
        "sendCnt": 3
    }
    ```
  - 응답
    ```
    {
      "timestamp": "2021-03-02T05:31:26.453",
      "status": 200,
      "message": "정상적으로 처리되었습니다.",
      "result": {
          "token": "zPq"
      }
    }
    ```
3. 받기 API
  - 제약조건
    -  token : 3자리 문자
  - 요청
    ```
    PUT /sendMoney
    X-USER-ID: ${userId}
    X-ROOM-ID: ${roomId}

    {
        "token": "zPq"
    }
    ```
    -응답
    ```
      {
      "timestamp": "2021-03-02T05:33:51.045",
      "status": 200,
      "message": "정상적으로 처리되었습니다.",
      "result": {
          "recvAmt": 34
      }
    }
    ```
  - 오류
    -  K201 : 받을수 있는 대상아님(뿌린 본인, 다른방 존재)
    ```
      {
        "timestamp": "2021-03-02T06:16:57.574",
        "status": 400,
        "code": "K201",
        "message": "com.kakaopay.moneyfly.exception.details.RecvNotTargetException: 받을 수 있는 대상이 아닙니다."
      }
    ```
    -  K202 : 중복 받기 요청
    ```
      {
        "timestamp": "2021-03-02T06:18:04.898",
        "status": 400,
        "code": "K202",
        "message": "com.kakaopay.moneyfly.exception.details.RecvDupActException: 한번만 받을수 있습니다."
      }
    ```
    -  K203 : 받을수 있는 시간 만료(10분)
    ```
    {
      "timestamp": "2021-03-02T06:18:45.442",
      "status": 400,
      "code": "K203",
      "message": "com.kakaopay.moneyfly.exception.details.RecvExpTimeException: 받을 수 있는 시간이 만료되었습니다(10분)"
    }    
    ```
    -  K204 : 모든 받기 종료
    ```
    {
      "timestamp": "2021-03-02T06:19:18.096",
      "status": 400,
      "code": "K204",
      "message": "com.kakaopay.moneyfly.exception.details.RecvNotPossibleException: 모든 받기가 완료되었습니다."
    }
    ```
4. 조회 API
  - 제약조건
    -  token : 3자리 문자
  - 요청
     ```
      GET /sendMoney
      X-USER-ID: ${userId}

      {
          "token": "zPq"
      } 
     ```
 - 응답
    ```
      {
        "timestamp": "2021-03-02T05:35:15.126",
        "status": 200,
        "message": "정상적으로 처리되었습니다.",
        "result": {
            "sendTime": "2021-03-02T05:31:26",
            "sendAmt": 100,
            "cmpAmt": 34,
            "compleList": [
                {
                    "eachAmt": 34,
                    "userId": 2
                }
            ]
        }
    }
   ```
  - 오류
    -  K301 : 조회 권한 없음
    ```
    {
      "timestamp": "2021-03-02T06:19:54.946",
      "status": 400,
      "code": "K301",
      "message": "com.kakaopay.moneyfly.exception.details.InqrNotAuthException: 조회할 수 있는 권한이 없습니다."
    }
    ```
    -  K302 : 조회 기간 만료
    ```
    {
      "timestamp": "2021-03-02T06:20:44.257",
      "status": 400,
      "code": "K302",
      "message": "com.kakaopay.moneyfly.exception.details.InqrExpTimeException: 조회할 수 있는 기간이 만료되었습니다."
    }
    ```
