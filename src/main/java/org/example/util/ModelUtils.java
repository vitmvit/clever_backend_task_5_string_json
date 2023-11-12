package org.example.util;

import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.TestModel;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModelUtils {

    public static Customer getCustomer() {
        return new Customer(
                UUID.fromString("c3323c32-80c1-11ee-b962-0242ac120002"),
                "firstName",
                "LastName",
                LocalDate.now(),
                getListOrder()
        );
    }

    public static Order getOrder() {
        List<Product> list = new ArrayList<>(List.of(
                new Product(UUID.fromString("b2e10b3a-80c4-11ee-b962-0242ac120002"), "phone", 100.0)
        ));
        return new Order(UUID.fromString("c2a5102a-80c4-11ee-b962-0242ac120002"), list, OffsetDateTime.of(
                LocalDate.of(2015, 10, 18),
                LocalTime.of(11, 20, 30, 1000),
                ZoneOffset.ofHours(-5)));
    }

    public static Product getProduct() {
        return getListProduct().get(0);
    }

    public static TestModel getTestModel() {
        return new TestModel(
                (byte) 10,
                (short) 100,
                1000L,
                10000,
                3.14,
                2.718f,
                true,
                42,
                BigInteger.valueOf(1234567),
                getProduct(),
                getCustomer(),
                getListOrder(),
                getListProduct());
    }

    private static List<Product> getListProduct() {
        return new ArrayList<>(List.of(
                new Product(UUID.fromString("611dada2-8138-11ee-b962-0242ac120002"), "phone", 100.),
                new Product(UUID.fromString("5a2b3212-8138-11ee-b962-0242ac120002"), "car", 100.)
        ));
    }

    private static List<Order> getListOrder() {
        return new ArrayList<>(List.of(
                new Order(UUID.fromString("ba5d110e-80c1-11ee-b962-0242ac120002"), getListProduct(), OffsetDateTime.of(
                        LocalDate.of(2015, 10, 18),
                        LocalTime.of(11, 20, 30, 1000),
                        ZoneOffset.ofHours(-5))),
                new Order(UUID.fromString("3a53130e-80c1-11ee-b962-0242ac120002"), getListProduct(), OffsetDateTime.of(
                        LocalDate.of(2014, 11, 14),
                        LocalTime.of(10, 23, 37, 1000),
                        ZoneOffset.ofHours(-3)))
        ));
    }
}
