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

public class LlamaServiceTest {
    @Test
    public void sendPromptReturnsResponse() throws Exception {
        String expected = "ok";
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(expected.getBytes(StandardCharsets.UTF_8)));

        ConnectionFactory factory = url -> connection;
        LlamaService service = new LlamaService("http://test", factory);
        String result = service.sendPrompt("hi");

        assertThat(result, is(expected));
    }
}
