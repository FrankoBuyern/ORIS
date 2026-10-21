package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ReviewService;
import utils.RequestUtils;

import java.io.IOException;
import java.util.List;

public class ReviewServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ReviewServlet.class);
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        log.info("POST {} params={}", req.getRequestURI(), RequestUtils.formatParams(req.getParameterMap()));

        Integer productId = RequestUtils.parseProductId(req, resp);
        if (productId == null) {
            return;
        }

        String text = req.getParameter("text");
        if (text == null || text.isBlank()) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: параметр text обязателен");
            return;
        }

        int id = reviewService.add(productId, text);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().println("Отзыв добавлен, id=" + id);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        log.info("DELETE {} params={}", req.getRequestURI(), RequestUtils.formatParams(req.getParameterMap()));

        Integer id = RequestUtils.parseId(req, resp);
        if (id == null) {
            return;
        }

        if (!reviewService.delete(id)) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Ошибка: отзыв не найден");
            return;
        }
        resp.getWriter().println("Отзыв " + id + " удалён");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        log.info("GET {} params={}", req.getRequestURI(), RequestUtils.formatParams(req.getParameterMap()));

        List<String> all = reviewService.getAll();
        if (all.isEmpty()) {
            resp.getWriter().println("Отзывов нет");
            return;
        }
        for (String r : all) {
            resp.getWriter().println(r);
        }
    }
}