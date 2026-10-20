package org.example.repository;

import org.example.model.Car;
import org.example.model.Client;
import org.example.model.Order;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRepairOrderRepositoryTest {

    @Test
    void shouldSaveAndFindOrderById() {
        // Given (Дано: створюємо репозиторій та замовлення)
        RepairOrderRepository repository = new InMemoryRepairOrderRepository();
        Client client = new Client("Іван Петренко", "+380501234567");
        Car car = new Car("VIN987654321", "Skoda", "Octavia", 2022, client);
        Order order = new Order(car);

        // When (Коли: зберігаємо замовлення в репозиторій)
        repository.save(order);
        Optional<Order> foundOrder = repository.findById(order.getId());

        // Then (Тоді: перевіряємо, чи замовлення успішно знайшлося)
        assertTrue(foundOrder.isPresent());
        assertEquals(order.getId(), foundOrder.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        // Given (Дано: порожній репозиторій)
        RepairOrderRepository repository = new InMemoryRepairOrderRepository();

        Optional<Order> foundOrder = repository.findById("non-existent-id");

        // Then (Тоді отримуємо порожній результат)
        assertFalse(foundOrder.isPresent());
    }
}