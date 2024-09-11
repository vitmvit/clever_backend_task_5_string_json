package by.vitikova.parser.converter;

import by.vitikova.parser.converter.impl.JsonSerializerImpl;
import by.vitikova.parser.util.CustomerTestData;
import by.vitikova.parser.util.OrderTestData;
import by.vitikova.parser.util.ProductTestData;
import by.vitikova.parser.util.TestModelTestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonSerializerTest {

    private JsonSerializer jsonSerializer;

    static Stream<Object> objectToJson() {
        return Stream.of(
                ProductTestData.builder().build().buildProduct(),
                OrderTestData.builder().build().buildOrder(),
                CustomerTestData.builder().build().buildCustomer(),
                TestModelTestData.builder().build().buildTestModel());
    }

    @BeforeEach
    public void setup() {
        jsonSerializer = new JsonSerializerImpl();
    }

    @ParameterizedTest
    @MethodSource("objectToJson")
    void convertShouldReturnExpectedJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        var expectedJson = objectMapper.writeValueAsString(object);
        System.out.println(object);
        var actualJson = jsonSerializer.convert(object);

        assertEquals(expectedJson, actualJson);
    }
}