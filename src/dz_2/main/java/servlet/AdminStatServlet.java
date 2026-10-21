package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;
import service.UserService;
import utils.RequestUtils;

import java.io.IOException;

public class AdminStatServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AdminStatServlet.class);
    private static final String ADMIN_TOKEN = "super-secret-key-that-will-be-leaked-into-claude-code";
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        log.info("GET {} params={}", req.getRequestURI(), RequestUtils.formatParams(req.getParameterMap()));

        String token = req.getHeader("Admin-Token");
        if (token == null || token.isBlank()) {
            log.warn("Admin stats: token missing");
            RequestUtils.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Ошибка: требуется заголовок Admin-Token");
            return;
        }
        if (!ADMIN_TOKEN.equals(token)) {
            log.warn("Admin stats: wrong token");
            RequestUtils.writeError(resp, HttpServletResponse.SC_FORBIDDEN, "Ошибка: неверный токен");
            return;
        }

        resp.getWriter().println("Пользователей: " + userService.count());
        resp.getWriter().println("Товаров: " + productService.count());
    }
}