package by.vitikova.parser.util;

import by.vitikova.parser.model.Product;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class ProductTestData {

    @Builder.Default
    private UUID id = UUID.fromString("611dada2-8138-11ee-b962-0242ac120002");

    @Builder.Default
    private String name = "phone";

    @Builder.Default
    private Double price = 100.;

    public Product buildProduct() {
        return new Product(id, name, price);
    }
}