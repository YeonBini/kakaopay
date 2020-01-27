# kakaopay

1. 개발 프레임워크
    - Spring Boot 2.2.4
    - 의존성
        - spring web
        - spring data jpa
        - spring security
        - h2
        - guava
        - commons-lang3
        - jjwt
        - querydsl
        - swagger
    
2. 문제 해결 전략
    - API와 관련해서는 /swagger-ui.html 을 통해서 확인할 수 있습니다. 

    <전략> 
    - Entity는 Builder 패턴을 구현하여 동시성 문제를 방어하고자 했습니다. 
    - jpa open-session-in-view를 false로 설정하여, lazy 로딩이 필요한 부분들은 서비스 레이어세서 모두 처리하였습니다. 
    - default_batch_fetch_size를 100으로 설정하여 N+1 이슈를 방지하였습니다. 
    - jwt를 생성하고 spring security를 통해 인증을 할 수 있도록 구현하였습니다. 
    - api response는 ApiResult dto를 통해 리턴할 수 있도록 만들었습니다. 
     
    <요구사항>
    - 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API 개발
        - 제공되는 데이터는 기본적으로 InitDb 클래스를 통해 인스턴스 생성 후 자동으로 입력될 수 있도록 만들었습니다. 
        - 추가적으로 주택금융을 추가를 위해서는 별도의 api(/institute/housing-fund/save)를 구현하였습니다. 
        - 해당 api를 사용하기 위해서는 먼저 로그인(인증)이 필요로 합니다. 
    - 주택금융 공급 금융기관(은행) 목록을 출력하는 API 를 개발하세요.
        - 금융기관 목록은 인증 없이 조회할 수 있도록 하였습니다. 
    - 년도별 각 금융기관의 지원금액 합계를 출력하는 API 를 개발하세요.
        - 연도별 지원금액 합계는 인증 없이 조회할 수 있도록 하였습니다. 
        - dto를 컨트롤러 단에서만 처리하기 위해서 변환 해당 dto로의 변환 로직은 컨트롤러 단에서 처리하였습니다. 
    - 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API 개발
        - 별도의 인증 없이 조회할 수 있도록 하였습니다. 
    - 전체 년도(2005~2016)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API 개발
        - 앞으로 은행 별로 별도의 api가 필요로 할 수 있을 것 같아 인터페이스를 작성 후 상속을 받아 컨트롤러를 구현하였습니다. 
    - 특정 은행의 특정 달에 대해서 2018년도 해당 달에 금융지원 금액을 예측하는 API 개발
        - 아래 주소에 나온 코드를 사용하여 예측을 할 수 있는 api를 구현하였습니다. 
        - 각 월별 데이터가 유의미하다 생각하여, 예측하는 월의 데이터를 입력값으로 하여 예측하였습니다. 
        - https://ryanharrison.co.uk/2013/10/07/java-regression-library-linear-model.html
    - JWT(Json Web Token)
        - jjwt 라이브러리를 활용하여 jwt 토큰을 생성하였고, 토큰에는 email, roles를 담았습니다. 
        - 인증이 필요한 api는 header에 "api_key" : "Bearer " + token 을 입력하면 됩니다. 
        - 토큰을 리프레시 할 수 있는 별도의 api도 구현하였습니다. 
        - 확인 순서는 회원가입 -> 로그인 -> 리프레시 순서대로 확인하시면 되고, 위 swagger 주소에서 확인하실 수 있습니다. 
3. 빌드 및 실행방법
    ```shell script
    $ mvn clean package 
    $ java -jar target/housing-fund-0.0.1-SNAPSHOT.jar
    ```
