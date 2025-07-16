package bc.utils.llama;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LlamaModelsServlet extends HttpServlet {
    private final LlamaAdminService service = new LlamaAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = service.listModels();
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(json);
    }
}
