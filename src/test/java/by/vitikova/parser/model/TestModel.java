package by.vitikova.parser.model;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class TestModel {

    private byte byteField;
    private short shortField;
    private long longField;
    private int intField;
    private double doubleField;
    private float floatField;
    private boolean booleanField;
    private Integer integerField;
    private BigInteger bigIntegerField;
    private Product product;
    private Customer customer;
    private List<Order> orderList;
    private List<Product> productList;
}