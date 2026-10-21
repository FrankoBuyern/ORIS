package service;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private static final List<String> products = new CopyOnWriteArrayList<>();

//    static {
//        products.add("1");
//        products.add("2");
//    }

    public List<String> getAll() {
        return new ArrayList<>(products);
    }

    public void add(String name) {
        products.add(name);
        log.info("Product added: {}", name);
    }

    public int count() {
        return products.size();
    }
}
