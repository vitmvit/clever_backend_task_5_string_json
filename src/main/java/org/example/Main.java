package org.example;

import org.example.converter.JsonDeserializer;
import org.example.converter.JsonSerializer;
import org.example.converter.impl.JsonDeserializerImpl;
import org.example.converter.impl.JsonSerializerImpl;
import org.example.model.TestModel;

import static org.example.util.ModelUtils.getTestModel;

public class Main {

    public static void main(String[] args) {

        JsonSerializer jsonConverter = new JsonSerializerImpl();
        JsonDeserializer jsonDeserializer = new JsonDeserializerImpl();

        String json = jsonConverter.convert(getTestModel());
        System.out.println(json);

        var product = jsonDeserializer.convert(json, TestModel.class);
        System.out.println(product);
    }
}