package bc.utils.llama;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LlamaPullServlet extends HttpServlet {
    private final LlamaAdminService service = new LlamaAdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            name = "llama3:8b";
        }
        String result = service.pullModel(name);
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write(result);
    }
}
