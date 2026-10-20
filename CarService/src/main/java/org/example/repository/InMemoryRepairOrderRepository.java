package org.example.repository;

import org.example.model.Order;
import org.example.model.OrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryRepairOrderRepository implements RepairOrderRepository {
    private final Map<String, Order> storage = new HashMap<>();

    @Override
    public void save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Замовлення не може бути null.");
        }
        storage.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return storage.values().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }
}