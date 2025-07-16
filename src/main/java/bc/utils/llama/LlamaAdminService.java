package bc.utils.llama;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LlamaAdminService {
    private final String host;
    private final ConnectionFactory factory;

    public LlamaAdminService() {
        this("http://workstation.loc:11434");
    }

    public LlamaAdminService(String host) {
        this(host, url -> (HttpURLConnection) url.openConnection());
    }

    public LlamaAdminService(String host, ConnectionFactory factory) {
        this.host = host;
        this.factory = factory;
    }

    public boolean ping() {
        try {
            URL url = new URL(host);
            HttpURLConnection conn = factory.create(url);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            return code >= 200 && code < 400;
        } catch (IOException e) {
            return false;
        }
    }

    public String listModels() throws IOException {
        URL url = new URL(host + "/api/tags");
        HttpURLConnection conn = factory.create(url);
        conn.setRequestMethod("GET");
        return readResponse(conn);
    }

    public String pullModel(String name) throws IOException {
        URL url = new URL(host + "/api/pull");
        HttpURLConnection conn = factory.create(url);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        String payload = String.format("{\"name\": \"%s\"}", escape(name));
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes(StandardCharsets.UTF_8));
        }
        return readResponse(conn);
    }

    private String readResponse(HttpURLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private String escape(String text) {
        return text.replace("\"", "\\\"");
    }
}
