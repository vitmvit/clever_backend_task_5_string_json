package by.vitikova.parser.util;

import by.vitikova.parser.model.Customer;
import by.vitikova.parser.model.Order;
import by.vitikova.parser.model.Product;
import by.vitikova.parser.model.TestModel;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Data
@Builder(setterPrefix = "with")
public class TestModelTestData {

    @Builder.Default
    private byte byteField = 10;

    @Builder.Default
    private short shortField = 100;

    @Builder.Default
    private long longField = 1000L;

    @Builder.Default
    private int intField = 10000;

    @Builder.Default
    private double doubleField = 3.14;

    @Builder.Default
    private float floatField = 2.718f;

    @Builder.Default
    private boolean booleanField = true;

    @Builder.Default
    private Integer integerField = 42;

    @Builder.Default
    private BigInteger bigIntegerField = BigInteger.valueOf(1234567);

    @Builder.Default
    private Product product = getListProduct().get(0);

    @Builder.Default
    private Customer customer = getCustomer();

    @Builder.Default
    private List<Order> orderList = getListOrder();

    @Builder.Default
    private List<Product> productList = getListProduct();

    @Builder.Default
    private Map<String, String> map = buildMap();

    public static Map<String, String> buildMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        return map;
    }

    public static Customer getCustomer() {
        return new Customer(
                UUID.fromString("c3323c32-80c1-11ee-b962-0242ac120002"),
                "firstName",
                "LastName",
                LocalDate.now(),
                getListOrder()
        );
    }

    private static List<Product> getListProduct() {
        return new ArrayList<>(List.of(
                new Product(UUID.fromString("611dada2-8138-11ee-b962-0242ac120002"), "phone", 100.)
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

    public TestModel buildTestModel() {
        return new TestModel(byteField, shortField, longField, intField, doubleField, floatField, booleanField, integerField, bigIntegerField, product, customer, orderList, productList);
    }
}