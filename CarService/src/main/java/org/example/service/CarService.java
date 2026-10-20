package org.example.service;

import org.example.model.Car;
import org.example.model.Client;
import org.example.model.Mechanic;
import org.example.model.Order;
import org.example.repository.RepairOrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarService {
    private final List<Client> clients = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private final List<Mechanic> mechanics = new ArrayList<>();

    private final RepairOrderRepository orderRepository;
    private final PriceCalculator priceCalculator;

    public CarService(RepairOrderRepository orderRepository, PriceCalculator priceCalculator) {
        if (orderRepository == null || priceCalculator == null) {
            throw new IllegalArgumentException("Репозиторій та калькулятор цін не можуть бути null.");
        }
        this.orderRepository = orderRepository;
        this.priceCalculator = priceCalculator;
    }

    public void registerClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Клієнт не може бути нульовим.");
        }
        clients.add(client);
    }

    public void registerCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Автомобіль не може бути нульовим.");
        }
        for (Car existingCar : cars) {
            if (existingCar.getVin().equalsIgnoreCase(car.getVin())) {
                throw new IllegalArgumentException("Автомобіль із VIN-кодом '" + car.getVin() + "' вже зареєстрований в автосервісі.");
            }
        }
        cars.add(car);
    }

    public void registerMechanic(Mechanic mechanic) {
        if (mechanic == null) {
            throw new IllegalArgumentException("Механік не може бути нульовим.");
        }
        mechanics.add(mechanic);
    }

    public Order createOrder(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Неможливо створити замовлення без автомобіля.");
        }
        if (!cars.contains(car)) {
            throw new IllegalArgumentException("Цей автомобіль не зареєстрований у сервісі.");
        }
        Order order = new Order(car);
        orderRepository.save(order);
        return order;
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public BigDecimal calculateOrderPrice(Order order) {
        return priceCalculator.calculateCommercialPrice(order);
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Car> getCars() {
        return cars;
    }

    public List<Mechanic> getMechanics() {
        return mechanics;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}