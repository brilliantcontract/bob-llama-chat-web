package bc.utils.llama;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LlamaService {
    private final String endpoint;
    private final ConnectionFactory factory;

    public LlamaService() {
        this("http://workstation.loc:11434/api/generate");
    }

    public LlamaService(String endpoint) {
        this(endpoint, url -> (HttpURLConnection) url.openConnection());
    }

    public LlamaService(String endpoint, ConnectionFactory factory) {
        this.endpoint = endpoint;
        this.factory = factory;
    }

    public String sendPrompt(String prompt) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = factory.create(url);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        String payload = String.format("{\"model\":\"llama3:8b\",\"prompt\":\"%s\"}", escape(prompt));
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes(StandardCharsets.UTF_8));
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(extractSegment(line));
            }
        }
        return sb.toString();
    }

    private String escape(String text) {
        return text.replace("\"", "\\\"");
    }

    private String extractSegment(String json) {
        int idx = json.indexOf("\"response\":");
        if (idx == -1) {
            return json;
        }
        int start = json.indexOf('"', idx + 11);
        if (start == -1) {
            return "";
        }
        start++;
        int end = json.indexOf('"', start);
        if (end == -1) {
            return "";
        }
        return json.substring(start, end);
    }
}
