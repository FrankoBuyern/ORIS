package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CartService;
import service.OrderService;
import utils.RequestUtils;

import java.io.IOException;
import java.util.List;

public class OrderServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(OrderServlet.class);
    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        log.info("POST {} params={}", req.getRequestURI(), RequestUtils.formatParams(req.getParameterMap()));

        List<Integer> items = cartService.getAll();
        if (items.isEmpty()) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: корзина пуста");
            return;
        }

        String order = orderService.create(items);
        cartService.clear();

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().println("Заказ оформлен. " + order);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        log.info("GET {} params={}", req.getRequestURI(), RequestUtils.formatParams(req.getParameterMap()));

        List<String> orders = orderService.getAll();
        if (orders.isEmpty()) {
            resp.getWriter().println("Заказов нет");
            return;
        }
        for (String order : orders) {
            resp.getWriter().println(order);
        }
    }
}
