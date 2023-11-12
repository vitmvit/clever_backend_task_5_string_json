package org.example.model;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
    private Product product1;
    private Customer customer;
    private List<Order> orderList;
    private List<Product> productList;
}
