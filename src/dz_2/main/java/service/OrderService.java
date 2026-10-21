package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> orders = new CopyOnWriteArrayList<>();
    private static final AtomicInteger counter = new AtomicInteger(1);

    public String create(List<Integer> items) {
        int number = counter.getAndIncrement();
        String record = "Заказ #" + number + ": товары " + items;
        orders.add(record);
        log.info("Order created: {}", record);
        return record;
    }

    public List<String> getAll() {
        return new ArrayList<>(orders);
    }
}
