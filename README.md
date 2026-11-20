# 인사 만들기

이름을 입력해 기본·친근한·격식 있는 인사를 만들 수 있습니다. 결과는 복사할 수 있고, 성공한 인사는 현재 페이지에서 최근 5개까지 다시 볼 수 있습니다.

## Eclipse에서 Tomcat 11로 실행

1. Eclipse IDE for Enterprise Java and Web Developers에서 `GreetingApp`을 엽니다. 이미 열려 있으면 프로젝트를 새로 고침합니다(F5).
2. **Servers** 뷰에서 **Tomcat v11.0 Server at localhost**를 우클릭하고 **Add and Remove…**에서 **GreetingApp**을 추가합니다. 다른 워크스페이스에서는 **Apache Tomcat v11.0** 런타임을 먼저 등록합니다.
3. 실행할 때 **Servers** 뷰에서 Tomcat을 시작하고 <http://localhost:8080/greeting/>을 엽니다.

Eclipse가 `web/`을 `/greeting`에 배포합니다. 프로젝트는 Dynamic Web Module 6.0과 Java 17 소스 수준을 사용합니다. Tomcat 11에 등록한 JDK 25로 구동할 수 있습니다.

## WAR로 배포

1. Apache Tomcat 11 또는 10.1과 JDK 17 이상을 준비합니다.
2. 프로젝트 폴더에서 `./build-war.ps1`을 실행하면 `dist/greeting.war`가 생성됩니다.
3. Tomcat이 정지된 상태에서 `greeting.war`를 Tomcat 설치 폴더의 `webapps`에 복사합니다. 별도 `CATALINA_BASE`를 쓰는 경우 해당 인스턴스의 `webapps`에 복사합니다.
4. 실행할 때 Tomcat의 `bin/startup.bat`를 열고 기본 설정 기준 <http://localhost:8080/greeting/>에 접속합니다.

[Apache Tomcat 배포 문서](https://tomcat.apache.org/tomcat-10.1-doc/deployer-howto.html)

화면과 인사 API는 `web/index.jsp`에 있습니다. Tomcat은 `WEB-INF/web.xml` 설정에 따라 `POST /greeting/api/greeting`을 같은 JSP로 연결합니다. 화면 스크립트는 `web/greeting.js`에 있습니다. 기존 Java 내장 서버도 `java --class-path ./web ./src/Main.java`로 사용할 수 있습니다.
