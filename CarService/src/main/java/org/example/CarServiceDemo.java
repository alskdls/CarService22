package org.example;

import org.example.model.*;
import org.example.service.CarService;
import org.example.repository.*;
import org.example.service.*;

import java.math.BigDecimal;

public class CarServiceDemo {
    public static void main(String[] args) {
        System.out.println("=== СТАРТ ДЕМОНСТРАЦІЇ АВТОСЕРВІСУ ===\n");

        RepairOrderRepository repository = new InMemoryRepairOrderRepository();
        PriceCalculator calculator = new PriceCalculator();
        CarService service = new CarService(repository, calculator);

        System.out.println("--- 1. Успішна реєстрація сутностей ---");
        Client client = new Client("Іван Петренко", "+380501234567");
        service.registerClient(client);
        System.out.println("Зареєстровано клієнта: " + client);

        Car car = new Car("VIN123456789", "Toyota", "Camry", 2021, client);
        service.registerCar(car);
        System.out.println("Зареєстровано автомобіль: " + car);

        Mechanic mechanic = new Mechanic("Олег Мастер", "Ходова частина");
        service.registerMechanic(mechanic);
        System.out.println("Зареєстровано механіка: " + mechanic);
        System.out.println();

        System.out.println("--- 2. Повний успішний життєвий цикл замовлення ---");
        Order order = service.createOrder(car);
        System.out.println("Створено замовлення: " + order.getStatus());

        Job job1 = new Job("Заміна гальмівних колодок", new BigDecimal("1500.00"), new BigDecimal("800.00"));
        Job job2 = new Job("Заміна мастила", new BigDecimal("1000.00"), new BigDecimal("400.00"));
        order.addJob(job1);
        order.addJob(job2);
        System.out.println("Додано робіт до замовлення. Кількість робіт: " + order.getJobs().size());

        order.diagnose();
        System.out.println("Статус після діагностики: " + order.getStatus());

        order.approve();
        System.out.println("Статус після погодження: " + order.getStatus());

        order.assignMechanic(mechanic);
        System.out.println("Призначено механіка: " + mechanic.getName());

        order.startWork();
        System.out.println("Статус виконання: " + order.getStatus());

        order.completeJob(0);
        order.completeJob(1);
        System.out.println("Усі роботи позначені як виконані.");

        order.completeOrder();
        System.out.println("Фінальний статус замовлення: " + order.getStatus());

        BigDecimal basePrice = calculator.calculateBasePrice(order);
        BigDecimal commercialPrice = service.calculateOrderPrice(order);
        System.out.println("Базова вартість замовлення: " + basePrice + " грн");
        System.out.println("Комерційна вартість (з урахуванням знижок): " + commercialPrice + " грн");

        var found = service.getOrderById(order.getId());
        System.out.println("Отримано замовлення з репозиторію за ID: " + (found.isPresent() ? "Успішно знайнено" : "Не знайдено"));
        System.out.println();

        System.out.println("--- 3. Перевірка негативних сценаріїв та винятків ---");

        System.out.print("А) Спроба створити і погодити порожнє замовлення: ");
        try {
            Car car2 = new Car("VIN987654321", "Honda", "Civic", 2019, client);
            service.registerCar(car2);
            Order emptyOrder = service.createOrder(car2);
            emptyOrder.approve();
        } catch (Exception e) {
            System.out.println("ПЕРЕХОПЛЕНО ПОМИЛКУ -> " + e.getMessage());
        }

        System.out.print("Б) Спроба зареєструвати авто з дублюючим VIN: ");
        try {
            Car duplicateCar = new Car("VIN123456789", "Ford", "Focus", 2020, client);
            service.registerCar(duplicateCar);
        } catch (Exception e) {
            System.out.println("ПЕРЕХОПЛЕНО ПОМИЛКУ -> " + e.getMessage());
        }

        System.out.print("В) Спроба додати роботу у вже завершене замовлення: ");
        try {
            Job extraJob = new Job("Миття кузова", BigDecimal.ZERO, new BigDecimal("300.00"));
            order.addJob(extraJob);
        } catch (Exception e) {
            System.out.println("ПЕРЕХОПЛЕНО ПОМИЛКУ -> " + e.getMessage());
        }

        System.out.println("\n=== ДЕМОНСТРАЦІЮ УСПІШНО ЗАВЕРШЕНО ===");
    }
}