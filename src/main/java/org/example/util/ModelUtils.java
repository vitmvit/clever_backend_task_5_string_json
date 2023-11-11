package org.example.util;

import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModelUtils {

    public static Customer getCustomer() {
        List<Product> list = new ArrayList<>(List.of(
                new Product(UUID.randomUUID(), "phone", 100.0),
                new Product(UUID.randomUUID(), "car", 100.0)
        ));
        List<Order> orderList = new ArrayList<>(List.of(
                new Order(UUID.randomUUID(), list, OffsetDateTime.of(
                        LocalDate.of(2015, 10, 18),
                        LocalTime.of(11, 20, 30, 1000),
                        ZoneOffset.ofHours(-5)))
        ));
        return new Customer(
                UUID.randomUUID(),
                "firstName",
                "LastName",
                LocalDate.now(),
                orderList
        );
    }
}
