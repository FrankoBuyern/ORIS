package utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import java.util.stream.Collectors;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public final class RequestUtils {

    private RequestUtils() {
    }

    public static String formatParams(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> e : map.entrySet()) {
            sb.append(e.getKey()).append("=").append(String.join(",", e.getValue())).append(", ");
        }
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 2);
        }
        return "{" + sb + "}";
    }

    public static void writeError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().println(message);
    }

    public static Map<String, String> parseBody(HttpServletRequest req) throws IOException {
        Map<String, String> result = new HashMap<>();

        String body = req.getReader().lines().collect(Collectors.joining());
        if (body.isBlank()) {
            return result;
        }

        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                result.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return result;
    }

    public static Integer parseId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id;
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: параметр id обязателен");
            return null;
        }

        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: параметр id обязателен");
            return null;
        }
        return id;
    }

    public static String parseName(HttpServletRequest req, HttpServletResponse resp, Map<String, String> body) throws IOException {
        String name = body.get("name");

        if (name == null || name.isBlank()) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: параметр name обязателен.");
            return null;
        }

        return name;
    }

    public static String parseEmail(HttpServletRequest req, HttpServletResponse resp, Map<String, String> body) throws IOException {
        String email = body.get("email");

        if (email == null || email.isBlank()) {
            RequestUtils.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: параметр email обязателен.");
            return null;
        }

        return email;
    }

    public static Integer parseProductId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("productId");
        if (param == null || param.isBlank()) {
            writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: параметр productId обязателен");
            return null;
        }
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка: productId должен быть числом");
            return null;
        }
    }

    public static boolean wantsHtml(HttpServletRequest req) {
        String accept = req.getHeader("Accept");
        return accept != null && accept.contains("text/html");
    }

    public static boolean isAjax(HttpServletRequest req) {
        return "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
    }

    public static void setFlash(HttpServletRequest req, String message) {
        req.getSession().setAttribute("flash", message);
    }

    public static void moveFlashToRequest(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("flash") != null) {
            req.setAttribute("flash", session.getAttribute("flash"));
            session.removeAttribute("flash");
        }
    }

    public static void forward(Logger log, HttpServletRequest req, HttpServletResponse resp, String jspName) throws ServletException, IOException {
        moveFlashToRequest(req);
        log.info("{} {} session={} -> FORWARD {}", req.getMethod(), req.getRequestURI(), req.getSession(false) != null, jspName);
        req.getRequestDispatcher("/WEB-INF/jsp/" + jspName).forward(req, resp);
    }

    public static void redirect(Logger log, HttpServletRequest req, HttpServletResponse resp, String url) throws IOException {
        log.info("{} {} session={} -> REDIRECT {}", req.getMethod(), req.getRequestURI(), req.getSession(false) != null, url);
        resp.sendRedirect(url);
    }


}
