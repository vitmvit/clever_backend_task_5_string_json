package by.vitikova.parser.util;

import by.vitikova.parser.model.Order;
import by.vitikova.parser.model.Product;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class OrderTestData {

    @Builder.Default
    private UUID id = UUID.fromString("c2a5102a-80c4-11ee-b962-0242ac120002");

    @Builder.Default
    private List<Product> products = List.of(
            new Product(UUID.fromString("b2e10b3a-80c4-11ee-b962-0242ac120002"), "phone", 100.0)
    );

    @Builder.Default
    private OffsetDateTime createDate = OffsetDateTime.of(
            LocalDate.of(2015, 10, 18),
            LocalTime.of(11, 20, 30, 1000),
            ZoneOffset.ofHours(-5));

    public Order buildOrder() {
        return new Order(id, products, createDate);
    }
}