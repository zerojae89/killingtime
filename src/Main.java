import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final int MAX_NAME_BYTES = 1024;

    public static void main(String[] args) throws IOException {
        String template = new String(readResource("/index.jsp"), StandardCharsets.UTF_8);
        int pageStart = template.indexOf("<!doctype html>");
        if (pageStart < 0) {
            throw new IOException("index.jsp에서 HTML 본문을 찾을 수 없습니다.");
        }
        byte[] page = template.substring(pageStart).getBytes(StandardCharsets.UTF_8);
        byte[] script = readResource("/greeting.js");
        byte[] styles = readResource("/greeting.css");


        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        server.createContext("/", exchange -> {
            try (exchange) {
                String path = exchange.getRequestURI().getPath();
                if (path.equals("/")) {
                    if (requireMethod(exchange, "GET")) {
                        send(exchange, 200, "text/html", page);
                    }
                } else if (path.equals("/greeting.js")) {
                    if (requireMethod(exchange, "GET")) {
                        send(exchange, 200, "text/javascript", script);
                    }
                } else if (path.equals("/greeting.css")) {
                    if (requireMethod(exchange, "GET")) {
                        send(exchange, 200, "text/css", styles);
                    }
                } else {
                    sendText(exchange, 404, "페이지를 찾을 수 없습니다.");
                }
            }
        });
        server.createContext("/api/greeting", Main::handleGreeting);
        server.start();
        System.out.println("인사 만들기: http://127.0.0.1:8080/");
    }

    private static byte[] readResource(String path) throws IOException {
        try (InputStream resource = Main.class.getResourceAsStream(path)) {
            if (resource == null) {
                throw new IOException(path + " 파일을 찾을 수 없습니다. 클래스패스에 web 폴더를 추가하세요.");
            }
            return resource.readAllBytes();
        }
    }


    private static void handleGreeting(HttpExchange exchange) throws IOException {
        try (exchange) {
            if (!exchange.getRequestURI().getPath().equals("/api/greeting")) {
                sendText(exchange, 404, "페이지를 찾을 수 없습니다.");
                return;
            }
            if (!requireMethod(exchange, "POST")) {
                return;
            }
            byte[] body = exchange.getRequestBody().readNBytes(MAX_NAME_BYTES + 1);
            if (body.length > MAX_NAME_BYTES) {
                sendText(exchange, 413, "이름이 너무 깁니다.");
                return;
            }
            String name = new String(body, StandardCharsets.UTF_8).strip();
            if (name.isEmpty()) {
                sendText(exchange, 400, "이름을 입력해 주세요.");
                return;
            }
            sendText(exchange, 200, "안녕하세요, " + name + "님!");
        }
    }

    private static boolean requireMethod(HttpExchange exchange, String method) throws IOException {
        if (method.equals(exchange.getRequestMethod())) {
            return true;
        }
        exchange.getResponseHeaders().set("Allow", method);
        sendText(exchange, 405, "지원하지 않는 요청 방식입니다.");
        return false;
    }

    private static void sendText(HttpExchange exchange, int status, String body) throws IOException {
        send(exchange, status, "text/plain", body.getBytes(StandardCharsets.UTF_8));
    }

    private static void send(HttpExchange exchange, int status, String type, byte[] body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", type + "; charset=UTF-8");
        exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        exchange.sendResponseHeaders(status, body.length);
        exchange.getResponseBody().write(body);
    }
}
