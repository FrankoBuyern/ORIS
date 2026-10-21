package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;
import java.util.Map;

import service.ProductService;
import utils.RequestUtils;

import static utils.RequestUtils.*;

public class ProductServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ProductServlet.class);
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        log.info("GET {} params={} session={}", req.getRequestURI(), formatParams(req.getParameterMap()), req.getSession(false) != null);
        List<String> allProducts = productService.getAll();

        if(wantsHtml(req)) {
            req.setAttribute("products", allProducts);
            forward(log, req, resp, "products.jsp");
            return;
        }

        resp.setContentType("text/plain; charset=UTF-8");

        PrintWriter printWriter = resp.getWriter();

        if (allProducts.isEmpty()) {
            printWriter.println("Товаров нет");
        } else {
            String allProductsString = String.join(", ", allProducts);
            printWriter.println(allProductsString);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        log.info("{} {} params={}", req.getMethod(), req.getRequestURI(), formatParams(req.getParameterMap()));

        Map<String, String> body = RequestUtils.parseBody(req);
        String name = parseName(req, resp, body);

        productService.add(name);
        resp.setStatus(HttpServletResponse.SC_CREATED);

        resp.getWriter().println("Товар добавлен: " + name);
    }
}

