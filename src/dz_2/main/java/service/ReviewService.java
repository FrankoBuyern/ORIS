package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReviewService {
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private static final Map<Integer, String> reviews = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(1);

    public int add(int productId, String text) {
        int id = counter.getAndIncrement();
        reviews.put(id, "Отзыв #" + id + " к товару " + productId + ": " + text);
        log.info("Review {} added for product {}", id, productId);
        return id;
    }

    public boolean delete(int id) {
        boolean removed = reviews.remove(id) != null;
        if (removed) {
            log.info("Review {} deleted", id);
        } else {
            log.warn("Review {} not found", id);
        }
        return removed;
    }

    public List<String> getAll() {
        return new ArrayList<>(reviews.values());
    }
}