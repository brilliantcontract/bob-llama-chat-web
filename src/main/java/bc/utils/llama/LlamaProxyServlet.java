package bc.utils.llama;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LlamaProxyServlet extends HttpServlet {
    private final LlamaService service = new LlamaService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String prompt = req.getParameter("prompt");
        String result = service.sendPrompt(prompt == null ? "" : prompt);
        resp.setContentType("application/json");
        resp.getWriter().write(result);
    }
}
