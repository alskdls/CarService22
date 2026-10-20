package org.example.repository;

import org.example.model.Order;
import org.example.model.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface RepairOrderRepository {
    void save(Order order);
    Optional<Order> findById(String id);
    List<Order> findAll();
    List<Order> findByStatus(OrderStatus status);
}