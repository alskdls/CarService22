package org.example.service;

import org.example.model.Job;
import org.example.model.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceCalculator {
    private static final BigDecimal MIN_PRICE = new BigDecimal("500.00");
    private static final BigDecimal DISCOUNT_THRESHOLD = new BigDecimal("5000.00");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    public BigDecimal calculateBasePrice(Order order) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Job job : order.getJobs()) {
            sum = sum.add(job.getTotalCost());
        }
        return sum.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateCommercialPrice(Order order) {
        BigDecimal basePrice = calculateBasePrice(order);
        BigDecimal finalPrice = basePrice;

        if (basePrice.compareTo(DISCOUNT_THRESHOLD) > 0) {
            BigDecimal discount = basePrice.multiply(DISCOUNT_RATE);
            finalPrice = basePrice.subtract(discount);
        }

        if (finalPrice.compareTo(MIN_PRICE) < 0 && finalPrice.compareTo(BigDecimal.ZERO) > 0) {
            finalPrice = MIN_PRICE;
        }

        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}