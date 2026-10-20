package org.example.service;

import org.example.model.Car;
import org.example.model.Client;
import org.example.model.Job;
import org.example.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCalculatorTest {

    @Test
    void shouldCalculateBasePriceCorrectly() {
        Client client = new Client("Іван Петренко", "+380501234567");
        Car car = new Car("VIN123456789", "Toyota", "Camry", 2021, client);
        Order order = new Order(car);

        Job job = new Job("Діагностика", new BigDecimal("200.00"), new BigDecimal("800.00"));
        order.addJob(job);

        PriceCalculator calculator = new PriceCalculator();

        BigDecimal basePrice = calculator.calculateBasePrice(order);

        assertEquals(new BigDecimal("1000.00"), basePrice);
    }
}