# 풀리팀 백엔드 채용과제 

유경호 지원자, 백엔드 개발자 채용 과제 전형 건

```
선생님과 학생간의 햑습지 관련 서버를 만드는 과제입니다.
```

## 비기능적 요구사항

- 과제 진행은 개인 **github public repository** 생성하여 진행 해주시기 바랍니다.
- 데이터베이스는 **h2**를 사용해서 개발해주세요.
- 서버 호스트는 **localhost(127.0.0.1)**, 서버 포트는 **8080**으로 설정해주세요.
- 다른 사람이 **읽기 좋은 코드**로 개발해주세요.
- 개발 언어는 [Kotlin](https://kotlinlang.org/) 입니다.
- Spring Boot를 사용해서 개발해주세요.
    - [Spring Boot 2.x](https://spring.io/projects/spring-boot) 이상 버전을 사용해서 개발해주세요.
- **성능과 비용 측면**도 함께 고려해서 작성해주세요.
- api endpoint는 예시대로 작성하고 requestbody, response등은 자유롭게 작성해주시면 됩니다.

---

## 요구사항

- 유저는 선생님 혹은 학생입니다.
- 어떤 과목인지에 대해서는 고려하지 않으셔도 됩니다.
- 개발이 필요한 API 총 **6개**입니다.

- 과제 활용 데이터(문제, 유형 정보)  아래 엑셀파일을 다운받아 활용하세요

  [backend_recruit_data.xlsx](%E1%84%91%E1%85%AE%E1%86%AF%E1%84%85%E1%85%B5%E1%84%90%E1%85%B5%E1%86%B7%20%E1%84%87%E1%85%A2%E1%86%A8%E1%84%8B%E1%85%A6%E1%86%AB%E1%84%83%E1%85%B3%20%E1%84%8E%E1%85%A2%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%80%E1%85%AA%E1%84%8C%E1%85%A6%20152cd2f77135803cb53fc94dd4257b79/backend_recruit_data.xlsx)

  problem : 문제

  unit_code: 유형정보

- 위 엑셀 파일의 데이터를 기반으로 과제를 풀어주시면 됩니다.
- 위의 파일의 데이터는 예시이고 직접 수정하거나 추가해서 과제를 해결하셔도 괜찮습니다.

---

# 데이터베이스 설계 내용

### 요구사항을 분석하여 단어 추출

| 단어 | 유형   | 비고                                             |
| --- |------|------------------------------------------------|
| 유저 | 엔티티  |                                                |
| 선생님 | 속성   | 회원의 유형 중 하나                                    |
| 학생 | 속성   | 회원의 유형 중 하나                                    |
| 문제 | 엔티티  |                                                |  |
| 유형코드(unitCode) | 속성   |                                                |
| 난이도(level) | 속성   | 속성값 - 1, 2, 3, 4, 5                            |
| 문제 유형(problemType) | 속성   | 속성값 - 주관식, 객관식, 전체(SUBJECTIVE, SELECTION, ALL) |
| 학습지 | 엔티티  |                                                |
| 학습지 생성 | 관계   | 매핑 카디날리티 - 일대다 관계, 참여 개체 - 유저(선생), 학습지         |
| 학습지 이름 | 속성   |                                                |
| 만든 유저 정보 | 속성   |                                                |
| 학습지 출제 | 관계   | 매핑 카디날리티 - 다대다 관계, 참여 개체 - 유저(학생), 학습지         |
| 채점하기 | 관계   | 매핑 카디날리티 - 다대다 관계, 참여 개체 - 출체, 문제              |
| 채점 결과 | 속성   | 속성 값 - 맞음, 틀림                                  |

### 개념적 설계도 작성

![개념적 설계.png](static/Conceptual%20Design.png)

- 사각형 - 개체
- 타원 - 속성
- 마름모 - 관계
- 1, n, m - 매핑 카디날리티

### 논리적 설계도 작성

![논리적 설계.png](static/Logical%20Design.png)

- 일대다 관계는 외래키로 표현
- 다대다 관계는 릴레이션으로 변환
- 편의상 도메인 세부내용까지는 작성하지 않음

# API 구현 내용

### 1. `GET`  문제 조회

- 선생님은 총 문제 수, 유형코드 리스트, 난이도, 문제 유형(주관식, 객관식, 전체)을 조건으로 문제를 조회합니다.

**파라미터 설명**

- 총 문제수 (totalCount)
    - 총 문제 수는 최대 문제 수를 의미 하기도 합니다.
    - 아래 조건을 활용해서 만들 수 있는 문제의 수가 파라미터로 전달받은 총 문제 수보다 적다면 총 문제 수보다 적어도 괜찮습니다.
    - 아래 조건을 활용해서 만들 수 있는 문제의 수가 파라미터로 전달받은 총 문제 수보다 많다면 총 문제 수 만큼만 조회합니다.

- 유형 코드 리스트 (unitCodeList)
    - 파라미터로 전달받은 유형코드 리스트에서만 문제를 조회합니다.

- 문제 유형 (problemType)
    - 주관식, 객관식, 전체 총 3개 경우가 존재하며 전달받은 유형으로만 조회를 합니다.

- 난이도 (level)
    - 파라미터로 받을 수 있는 난이도에는 **상, 중, 하** 3가지가 존재합니다.
    - 문제에는 난이도가 1부터 5까지 존재하고 숫자가 클수록 어려운 문제입니다.
    - 난이도 1 인 문제 - 하
    - 난이도 2,3,4 인 문제 - 중
    - 난이도 5 인 인 문제 - 상
    - 난이도별 문제 비율은 아래와 같습니다. 전체 수는 파라미터로 받은 **총 문제 수**가 기준입니다.
    - 파라미터로 전달받은 총문제 수를 아래 비율로 나눌 수 없는 경우 자유롭게 구현하시면 됩니다.
- 상 선택시 : **하** 문제 20%, **중** 문제 30%, **상** 문제 50%
- 중 선택시 : **하** 문제 25%, **중** 문제 50%, **상** 문제 25%
- 하 선택시 : **하** 문제 50%, **중** 문제 30%, **상** 문제 20%

- 필터링 순서는 유형코드 리스트 → 문제 유형 → 난이도 로 해주시면 됩니다.

```json
**REQUEST**
ex ) http://localhost:8080/problems?totalCount=15&unitCodeList=a,b,c&level=HIGH&problemType=SELECTION

totalCount : 총문제 수
unitCodeList : 유형코드 리스트
level : 난이도 (LOW, MIDDLE, HIGH)
problemType: 주관식, 객관식, 전체 (ALL, SUBJECTIVE, SELECTION)

**RESPONSE
{
  "success": true,
  "response": {
  "problemList": [
    {
      "id": 1550,
      "answer": "4",
      "unitCode": "uc1523",
      "level": 1,
      "problemType": "SUBJECTIVE"
    },
    {
      "id": 1538,
      "answer": "4",
      "unitCode": "uc1584",
      "level": 1,
      "problemType": "SUBJECTIVE"
    },
    {
      "id": 1536,
      "answer": "4",
      "unitCode": "uc1584",
      "level": 1,
      "problemType": "SUBJECTIVE"
    }
  ]
}**
```

### 2. `POST`  학습지 생성

- 선생님은 **1번에서 조회했던 문제 리스트**를 바탕으로 학습지를 생성합니다.
- 학습지 생성 시 포함될 수 있는 최대 문제 수는 50개 입니다.
- 학습지는 아래의 정보를 가지고 있습니다.
    - 학습지 이름
    - 만든 유저 정보

```json
**REQUEST**
ex ) http://localhost:8080/piece

**REQUESTBODY
{
  "userId": 1,
  "workbookName": "테스트 학습지",
  "problemIdList": [
    1550,
    1536,
    1538,
    1540
  ]
}

RESPONSEBODY
{
  "success": true,
  "response": {
    "workbookId": 1,
    "workbookName": "테스트 학습지",
    "userInfo": {
      "userId": 1,
      "type": "TEACHER"
    },
    "problemList": [
      {
        "id": 1536,
        "answer": "4",
        "unitCode": "uc1584",
        "level": 1,
        "problemType": "SUBJECTIVE"
      },
      {
        "id": 1538,
        "answer": "4",
        "unitCode": "uc1584",
        "level": 1,
        "problemType": "SUBJECTIVE"
      },
      {
        "id": 1540,
        "answer": "4",
        "unitCode": "uc1583",
        "level": 1,
        "problemType": "SUBJECTIVE"
      },
      {
        "id": 1550,
        "answer": "4",
        "unitCode": "uc1523",
        "level": 1,
        "problemType": "SUBJECTIVE"
      }
    ]
  },
  "status": 201,
  "message": "생성을 완료하였습니다."
}**

```

### 3. `POST`  학생에게 학습지 출제하기

- 선생님은 학생에게 **2번 문제에서 생성했던 학습지** **1개의 학습지**를 출제합니다.
- 선생님은 자신이 만든 학습지만 학생에게 출제가 가능합니다.
- 학습지는 **동시에 2명이상의 학생에게** 출제가 가능합니다.
- 이미 존재하는 학습지를 부여받는 경우 에러로 간주하지 않습니다.
- 만약 동시에 2명 이상의 학생에게 1개의 학습지를 출제하는데 이미 같은 학습지를 받은 경우가 있는 경우 이미 같은 학습지를 받은 학생을 제외하고 나머지 인원만 학습지를 받습니다.

```json
**REQUEST**
ex ) http://localhost:8080/piece/{pieceId}?studentIds=1,2

**RESPONSEBODY
{
  "success": true,
  "response": {
    "settingList": [
      {
        "settingId": 1,
        "userId": 3,
        "workbookId": 1
      },
      {
        "settingId": 2,
        "userId": 4,
        "workbookId": 1
      },
      {
        "settingId": 3,
        "userId": 5,
        "workbookId": 1
      }
    ]
  },
  "status": 201,
  "message": "생성을 완료하였습니다."
}**

```

### 4. `GET` 학습지의 문제 조회하기

- 학생은 자신에게 출제된 학습지의 문제 목록을 확인할 수 있습니다.
- 학습지 1개에 대한 문제목록을 확인하는 것입니다.
- 클라이언트는 이 api를 바탕으로 문제풀이 화면을 구현합니다.

```json
**REQUEST**
ex ) http://localhost:8080/piece/problems?pieceId=1

**RESPONSEBODY
{
  "success": true,
  "response": {
    "workbookId": 1,
    "workbookName": "테스트 학습지",
    "userInfo": {
      "userId": 1,
      "type": "TEACHER"
    },
    "problemList": [
      {
        "id": 1536,
        "answer": "4",
        "unitCode": "uc1584",
        "level": 1,
        "problemType": "SUBJECTIVE"
      },
      {
        "id": 1538,
        "answer": "4",
        "unitCode": "uc1584",
        "level": 1,
        "problemType": "SUBJECTIVE"
      },
      {
        "id": 1540,
        "answer": "4",
        "unitCode": "uc1583",
        "level": 1,
        "problemType": "SUBJECTIVE"
      },
      {
        "id": 1550,
        "answer": "4",
        "unitCode": "uc1523",
        "level": 1,
        "problemType": "SUBJECTIVE"
      }
    ]
  },
  "status": 200,
  "message": "작업을 완료하였습니다."
}**

```

### 5. `PUT` 채점하기

- 학생은 4번 문제에서 조회했던 문제들을 채점할 수 있습니다.
- 문제는 2개이상 한번에 채점이 가능합니다.
- 채점 결과는 맞음, 틀림 2가지가 존재합니다.

```json
**REQUEST**
ex ) http://localhost:8080/piece/problems?pieceId=1

**REQUESTBODY
{
  "gradingList": [
    {
      "problemId": 1536,
      "grade": true
    },
    {
      "problemId": 1538,
      "grade": false
    },
    {
      "problemId": 1540,
      "grade": true
    },
    {
      "problemId": 1550,
      "grade": false
    }
  ]
}

RESPONSEBODY
{
	자유롭게 구현하시면 됩니다.
}**
```

### 6. `GET` 학습지 학습 통계 분석하기

- 선생님은 1개의 학습지에 대해 학생들의 **학습 통계**를 파악할 수 있습니다.
- 선생님은 자신이 만든 학습지에 대해 학생들의 학습 통계 데이터를 분석할 수 있습니다.
- 선생님은 조회한 1개의 학습지에 대해 아래의 정보들을 파악 할 수 있습니다.
    - 학습지 ID
    - 학습지 이름
    - 출제한 학생들의 목록
    - 학생들의 학습 데이터
        - 학생 개별의 학습지 정답률
    - 학습지의 문제별 정답률 (출제받은 학생들에 한에서)

```json
**REQUEST**
ex ) http://localhost:8080/piece/analyze?pieceId=1

**RESPONSEBODY
{
  "success": true,
  "response": {
    "workbookId": 1,
    "workbookName": "테스트 학습지",
    "studentInfoList": [
      {
        "studentId": 3,
        "correctAnswerCount": 4,
        "totalAnswerCount": 4,
        "workbookCorrectAnswerRate": 100
      },
      {
        "studentId": 4,
        "correctAnswerCount": 2,
        "totalAnswerCount": 4,
        "workbookCorrectAnswerRate": 50
      },
      {
        "studentId": 5,
        "correctAnswerCount": 1,
        "totalAnswerCount": 4,
        "workbookCorrectAnswerRate": 25
      }
    ],
    "correctAnswerRateList": [
      {
        "problemId": 1536,
        "correctAnswerCount": 2,
        "totalAnswerCount": 3,
        "correctAnswerRate": 66
      },
      {
        "problemId": 1538,
        "correctAnswerCount": 1,
        "totalAnswerCount": 3,
        "correctAnswerRate": 33
      },
      {
        "problemId": 1540,
        "correctAnswerCount": 3,
        "totalAnswerCount": 3,
        "correctAnswerRate": 100
      },
      {
        "problemId": 1550,
        "correctAnswerCount": 1,
        "totalAnswerCount": 3,
        "correctAnswerRate": 33
      }
    ]
  },
  "status": 200,
  "message": "작업을 완료하였습니다."
}**
```

---

# 발생할 수 있는 위험요소와 해결 방안

## 1. 학습지 생성시 다량의 insert 문 발생 여지와 성능 저하 가능성

### 일어날 수 있는 상황

- 학습지 생성시에 학습지와 문제 엔티티의 n:m 연관관계에 포함(inclusion) 엔티티가 생성됨.
- 현재 학습지 생성시 최대 문제수가 50이며, 동시에 많은 트래픽이 일어날 수 있는 여지가 있기 때문에 한 번에 대량의 inclusion 데이터의 insert 쿼리가 발생할 수 있음.
- 필자는 JPA를 통한 Persistence 계층을 구현하였고, 엔티티 ID 생성 방식을 보편적인 방식인 IDENTITY 방식을 채택하고 있음.
- 이런 경우에 JPA를 통해 다량의 inclusion 데이터를 insert시, 대량의 insert 문이 발생할 수 있음. 이는 성능저하로 이어질 가능성이 있음.

### 해결책

- JPA에는 1차 캐시를 통한 쓰기 지연 기능을 제공함. `entityManager.persist()`가 호출되면 데이터베이스 시퀀스에 먼저 ID값을 받아온 뒤 엔티티를 버퍼에 저장해놨다가 `tx.commit()` 시점에 받아온 아아디를 가지고 insert 쿼리를 던지는 것임.
- `entityManager.persist()`되는 엔티티의 키 생성 방식이 **IDENTITY**라면 쓰기 지연이 일어나지 않음. 데이터베이스에 ID 값을 매번 실시간으로 받아와 isert를 하기 때문임.
- 엔티티의 키 생성 전략을 **SEQUENCE**를 사용하면 데이터베이스에서 allocationSize 만큼 id 값을 미리 받아놔 JPA의 쓰기 지연 기능을 활용할 수 있음.
- 이와 더불어 Hibernate에 order_inserts, order_updates, batch_size, batch_versioned_data 등과 같은 옵션과 MySQL, postgreSQL에 rewriteBatchedStatements=true, Insert 합치기 옵션 적용하면 벌크 인서트 가능해짐.
- 이와 같은 설정을 적용하여 학습지 생성시 일어나는 대량의 insert 쿼리를 하나의 insert 쿼리로 줄일 수 있을 것임.

## 2. 통계 데이터의 중복 조회로 인한 불필요한 리소스 낭비 가능성

### 일어날 수 있는 상황

- 통계 데이터는 상대적으로 조회시 쿼리가 복잡하고 거대한 편임.
- 또한 상대적으로 잘 변하지 않는 성격의 데이터가 많음.
- 이러한 복잡하고 큰 데이터를 매번 조회하게 되면 조회 성능을 많이 잡아먹게 되는 것임.

### 해결책

- 한 번 조회가 일어난 통계 데이터를 캐싱하여 들고 있다가, 중복된 조회 요청이 오면 캐시된 데이터를 내려주도록 하여 조회 성능을 올릴 수 있음.
- 단, 캐싱을 활용한다면 캐싱 정책을 잘 세우는 것이 중요함. 언제 캐싱을 할 것이며, 언제 삭제, 갱신할 것인지 잘 정해줘야 함.
- 또한 캐싱을 적용함으로써 성능 저하가 일어날 수 있는 상황이 있으니 주의하여 사용하여야 함.

## 3. 테스트 코드의 부재로 인한 사이드 이펙트 가능성

### 일어날 수 있는 상황

- 애플리케이션이란 그 하나가 크고 작은 모듈의 집합체임. 하나로 동작하는 물체가 아닌 수많은 모듈들이 맞물려 돌아간다는 의미임.
- 또한 소프트웨어는 완성된 후에도 지속적으로 수정이 일어나는 성격을 지님.
- 이러한 상황에서 테스트 코드가 작성되어있지 않으면, 불특정 모듈이 수정될 시 어디서 영향을 받을지 파악이 힘들 수 있음.
- 또한 마이크로서비스 환경과 에자일 프로세스를 지닌 비교적 개발 사이클이 빠른 조직이라면 더 큰 문제가 됨.
- 다른 서비스 간 종속성을 끊어내고 CI/CD 파이프라인 구축을 통해 빠르게 유저들의 요구사항을 반영하며 발전해나가야 하는데, 버그를 잡느라 그러지 못할 수도 있음.

### 해결책

- 독립적인 단위 테스트를 작성하여 빌드, 배포 시 자동으로 애플리케이션을 테스트하도록 만들어야 함.
- 테스트를 꼼꼼히 잘 작성하여 문제 발생시 빠르게 발견하여 수정할 수 있도록 해야함.
- 더 나아가 **SonarQube**와 같은 정적 분석기 제품도 적용하여 코드 커버리지를 지속적으로 높여나가야 함.

## 4. 분산되지 않은 환경에서 발생할 수 있는 문제점

### 일어날 수 있는 상황

- 현재 프로젝트는 과제 유형의 소규모 프로젝트라고는 하나, 로컬서버 하나로 동작하는 형태임.
- 이는 서버가 다운 됐을시 서비스가 마비될 수 있음을 의미함.

### 해결책

- 상황에 맞는 카오스 엔지니어링을 적용해야 함. 작게는 서비스 단위, 크게는 애플리케이션, 데이터베이스를 논리적으로나 물리적으로나 분산하여 운영하는 것이 필요함.
- 끊기지 않고 안정적인 서비스를 지속하기 위해서는 반드시 분산 환경 구축이 필요함.

---
