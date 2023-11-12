package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.util.ModelUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonConverterTest {
    private JsonConverter jsonConverter;

    static Stream<Object> objectToConvert() {
        return Stream.of(
                ModelUtils.getCustomer(),
                ModelUtils.getProduct(),
                ModelUtils.getOrder(),
                ModelUtils.getTestModel());
    }

    @BeforeEach
    public void setup() {
        jsonConverter = new JsonConverter();
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
}
