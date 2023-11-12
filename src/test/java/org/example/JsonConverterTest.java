package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.converter.impl.JsonConverterImpl;
import org.example.model.Product;
import org.example.util.ModelUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.example.util.ModelUtils.getProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonConverterTest {
    private JsonConverterImpl jsonConverter;

    static Stream<Object> objectToConvert() {
        return Stream.of(
                getProduct(),
                ModelUtils.getOrder(),
                ModelUtils.getCustomer(),
                ModelUtils.getTestModel());
    }

    @BeforeEach
    public void setup() {
        jsonConverter = new JsonConverterImpl();
    }

    @ParameterizedTest
    @MethodSource("objectToConvert")
    void convertShouldReturnExpectedJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String expectedJson = objectMapper.writeValueAsString(object);
        String actualJson = jsonConverter.convert(object);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void convertShouldReturnExpectedProductObject() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Product expectedObject = getProduct();
        String json = objectMapper.writeValueAsString(expectedObject);
        Product actualObject = jsonConverter.convert(json, Product.class);
        assertEquals(expectedObject.getId(), actualObject.getId());
        assertEquals(expectedObject.getName(), actualObject.getName());
        assertEquals(expectedObject.getPrice(), actualObject.getPrice());
    }
}
