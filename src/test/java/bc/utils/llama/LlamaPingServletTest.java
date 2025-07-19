package bc.utils.llama;

import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LlamaPingServletTest {
    @Test
    public void returns503WhenPingFails() throws Exception {
        LlamaAdminService service = Mockito.mock(LlamaAdminService.class);
        when(service.ping()).thenReturn(false);
        LlamaPingServlet servlet = new LlamaPingServlet(service);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        assertThat(sw.toString(), is("fail"));
    }

    @Test
    public void returnsOkWhenPingSucceeds() throws Exception {
        LlamaAdminService service = Mockito.mock(LlamaAdminService.class);
        when(service.ping()).thenReturn(true);
        LlamaPingServlet servlet = new LlamaPingServlet(service);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doGet(req, resp);

        assertThat(sw.toString(), is("ok"));
    }
}
