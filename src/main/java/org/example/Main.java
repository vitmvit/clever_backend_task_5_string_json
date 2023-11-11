package org.example.util;

import org.example.JsonConverter;

public class Main {

    public static void main(String[] args) {

        JsonConverter jsonConverter = new JsonConverter();

        var customer = ModelUtils.getCustomer();

        String json = jsonConverter.convert(customer.getOrders().get(0));
        System.out.println(json);
    }
}