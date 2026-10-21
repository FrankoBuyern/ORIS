package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CartService;
import utils.RequestUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import static utils.RequestUtils.formatParams;
import static utils.RequestUtils.parseProductId;

public class CartServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(CartServlet.class);
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        log.info("GET {} params={}", req.getRequestURI(), formatParams(req.getParameterMap()));


        PrintWriter printWriter = resp.getWriter();

        List<Integer> items = cartService.getAll();

        if (items.isEmpty()) {
            printWriter.println("Корзина пуста");
        } else {
            String cartsString = items.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            printWriter.println(cartsString);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        log.info("POST {} params={}", req.getRequestURI(), formatParams(req.getParameterMap()));

        PrintWriter printWriter = resp.getWriter();

        Integer productId = parseProductId(req, resp);
        if (productId == null) {
            return;
        }

        cartService.add(productId);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        printWriter.println("Продукт " + productId + " был добавлен в корзину");

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        log.info("DELETE {} params={}", req.getRequestURI(), formatParams(req.getParameterMap()));

        Integer productId = parseProductId(req, resp);
        if (productId == null) {
            return;
        }

        if (!cartService.remove(productId)) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Товара нет в корзине.");
        } else {
            resp.getWriter().println("Товар " + productId + " удалён из корзины");
        }
    }
}
