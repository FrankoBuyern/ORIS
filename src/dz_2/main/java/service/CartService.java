package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CartService {
    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    private static final List<Integer> cart = new CopyOnWriteArrayList<>();

    public void add(int productId) {
        cart.add(productId);
        log.info("Продукт {} добавлен в корзину", productId);
    }

    public List<Integer> getAll() {
        return new ArrayList<>(cart);
    }

    public boolean remove(int productId) {
        log.info("Продукт {} был удален из корзины", productId);
        return cart.remove(Integer.valueOf(productId));
    }

    public void clear() {
        cart.clear();
        log.info("Корзина очищена");
    }
}
