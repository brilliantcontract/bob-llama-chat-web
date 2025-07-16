package bc.utils.llama;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class LlamaAdminServiceTest {
    @Test
    public void pingReturnsTrueOn200() throws Exception {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        ConnectionFactory factory = url -> connection;
        LlamaAdminService service = new LlamaAdminService("http://test", factory);
        assertThat(service.ping(), is(true));
    }

    @Test
    public void listModelsReturnsResponse() throws Exception {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)));
        ConnectionFactory factory = url -> connection;
        LlamaAdminService service = new LlamaAdminService("http://test", factory);
        assertThat(service.listModels(), is("{}"));
    }

    @Test
    public void pullModelSendsPayload() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getOutputStream()).thenReturn(os);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("ok".getBytes(StandardCharsets.UTF_8)));
        ConnectionFactory factory = url -> connection;
        LlamaAdminService service = new LlamaAdminService("http://test", factory);
        String result = service.pullModel("x");
        assertThat(result, is("ok"));
        assertThat(os.toString("UTF-8"), is("{\"name\": \"x\"}"));
    }
}
