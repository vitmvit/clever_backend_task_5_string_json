package by.vitikova.parser.util;

import by.vitikova.parser.model.Customer;
import by.vitikova.parser.model.Order;
import by.vitikova.parser.model.Product;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class CustomerTestData {

    @Builder.Default
    private UUID id = UUID.fromString("c3323c32-80c1-11ee-b962-0242ac120002");

    @Builder.Default
    private String firstName = "firstName";

    @Builder.Default
    private String lastName = "LastName";

    @Builder.Default
    private LocalDate dateBirth = LocalDate.now();

    @Builder.Default
    private List<Order> orders = getListOrder();

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

    public Customer buildCustomer() {
        return new Customer(id, firstName, lastName, dateBirth, orders);
    }
}