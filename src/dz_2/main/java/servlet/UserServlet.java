package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import utils.RequestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class UserServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);
    private final UserService userService = new UserService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Ошибка: параметр id обязателен");
            return;
        }

        Integer id = RequestUtils.parseId(req, resp);
        if (id == null) {
            return;
        }

        User user = userService.getById(id);
        if (user == null) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Ошибка: пользователь не найден");
            return;
        }

        resp.getWriter().println(user.toString());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        Map<String, String> body = RequestUtils.parseBody(req);
        log.info("{} {} query={} body={}", req.getMethod(), req.getRequestURI(), RequestUtils.formatParams(req.getParameterMap()), body);

        String name = RequestUtils.parseName(req, resp, body);

        String email = RequestUtils.parseEmail(req, resp, body);

        Integer id = RequestUtils.parseId(req, resp);
        if (id == null) {
            return;
        }

        if (!userService.update(id, name, email)) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Ошибка: пользователь не найден");
            return;
        }
        resp.getWriter().println("Пользователь обновлён");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equals(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        Map<String, String> body = RequestUtils.parseBody(req);
        log.info("PATCH {} query={} body={}", req.getRequestURI(), RequestUtils.formatParams(req.getParameterMap()), body);

        String status = body.get("status");

        Integer id = RequestUtils.parseId(req, resp);
        if (id == null) {
            return;
        }

        if (status == null || status.isBlank()) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: параметр status обязателен");
            return;
        }

        if (!status.equals("active") && !status.equals("blocked")) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: допустимые значения status: active, blocked");
            return;
        }



        if (!userService.updateStatus(id, status)) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Ошибка: пользователь не найден");
            return;
        }
        resp.getWriter().println("Статус обновлён");
    }
}
