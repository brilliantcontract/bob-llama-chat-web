package bc.utils.llama;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LlamaPingServlet extends HttpServlet {
    private final LlamaAdminService service;

    public LlamaPingServlet() {
        this(new LlamaAdminService());
    }

    LlamaPingServlet(LlamaAdminService service) {
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        boolean ok = service.ping();
        if (!ok) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        resp.getWriter().write(ok ? "ok" : "fail");
    }
}
