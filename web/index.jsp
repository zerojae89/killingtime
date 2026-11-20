<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" trimDirectiveWhitespaces="true" import="java.nio.charset.StandardCharsets" %><%
    if ("/api/greeting".equals(request.getServletPath())) {
        response.setContentType("text/plain; charset=UTF-8");
        response.setHeader("X-Content-Type-Options", "nosniff");
        String message;
        if (!"POST".equals(request.getMethod())) {
            response.setHeader("Allow", "POST");
            response.setStatus(405);
            message = "지원하지 않는 요청 방식입니다.";
        } else {
            int maxNameBytes = 1024;
            byte[] body = request.getInputStream().readNBytes(maxNameBytes + 1);
            String name = new String(body, StandardCharsets.UTF_8).strip();
            if (body.length > maxNameBytes) {
                response.setStatus(413);
                message = "이름이 너무 깁니다.";
            } else if (name.isEmpty()) {
                response.setStatus(400);
                message = "이름을 입력해 주세요.";
            } else {
                message = "안녕하세요, " + name + "님!";
            }
        }
        out.print(message);
        return;
    }
%>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>인사 만들기</title>
    <link rel="stylesheet" href="./greeting.css">
</head>
<body>
    <main>
        <h1>인사 만들기</h1>
        <form>
            <label for="name">이름</label>
            <div class="controls">
                <input id="name" name="name" autocomplete="given-name" maxlength="100" placeholder="이름을 입력하세요">
                <button type="submit">인사하기</button>
            </div>
            <div class="style-field">
                <label for="style">인사말 스타일</label>
                <select id="style">
                    <option value="default">기본</option>
                    <option value="casual">친근한 인사</option>
                    <option value="formal">격식 있는 인사</option>
                </select>
            </div>
        </form>
        <p id="result" role="status"></p>
        <button id="copy" class="secondary" type="button" hidden>복사</button>
        <span id="feedback" role="status"></span>
        <details id="history" hidden>
            <summary>최근 인사</summary>
            <ol></ol>
        </details>
    </main>
    <script src="./greeting.js"></script>
</body>
</html>
