package org.example;

import org.example.converter.impl.JsonConverterImpl;
import org.example.model.Product;

import static org.example.util.ModelUtils.getProduct;

public class Main {

    public static void main(String[] args) {

        JsonConverterImpl jsonConverter = new JsonConverterImpl();

        String json = jsonConverter.convert(getProduct());
        System.out.println(json);

        Product product = jsonConverter.convert(json, Product.class);
        System.out.println(product);
    }
}