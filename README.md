# 인사 만들기

이름을 입력하고 **인사하기**를 누르거나 Enter를 누르면 인사말이 나오는 Java Swing 앱입니다. Swing은 JDK에 포함되어 있습니다.

## Eclipse에서 실행

1. **File → Import → General → Existing Projects into Workspace**를 선택하세요.
2. **Select root directory**에 이 폴더를 지정하고 **GreetingApp → Finish**를 선택하세요. 현재 폴더에서 작업하려면 **Copy projects into workspace**의 체크를 해제하세요.
3. **Window → Preferences → Java → Installed JREs**에 JDK 17 이상이 기본으로 선택되어 있는지 확인하세요. JDK를 추가하려면 **Add → Standard VM**에서 JDK 설치 폴더를 지정하세요.
4. **Package Explorer**에서 `src` 아래의 `Main.java`를 우클릭하고 **Run As → Java Application**을 선택하세요.

## PowerShell에서 실행

JDK 17 이상을 설치하고 프로젝트 폴더에서 PowerShell을 열어 아래 명령을 실행하세요.

```powershell
java .\src\Main.java
```
