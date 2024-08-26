package by.vitikova.parser.converter;

import by.vitikova.parser.converter.impl.JsonDeserializerImpl;
import by.vitikova.parser.model.Customer;
import by.vitikova.parser.model.Order;
import by.vitikova.parser.model.Product;
import by.vitikova.parser.model.TestModel;
import by.vitikova.parser.util.CustomerTestData;
import by.vitikova.parser.util.OrderTestData;
import by.vitikova.parser.util.ProductTestData;
import by.vitikova.parser.util.TestModelTestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonDeserializerTest {

    private JsonDeserializer jsonDeserializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        jsonDeserializer = new JsonDeserializerImpl();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void convertShouldReturnExpectedProductObject() throws JsonProcessingException {
        var expectedObject = ProductTestData.builder().build().buildProduct();
        var json = objectMapper.writeValueAsString(expectedObject);

        var actualObject = jsonDeserializer.convert(json, Product.class);

        assertEquals(expectedObject.getId(), actualObject.getId());
        assertEquals(expectedObject.getName(), actualObject.getName());
        assertEquals(expectedObject.getPrice(), actualObject.getPrice());
    }

    @Test
    void convertShouldReturnExpectedCustomerObject() throws JsonProcessingException {
        var expectedObject = CustomerTestData.builder().build().buildCustomer();
        var json = objectMapper.writeValueAsString(expectedObject);

        var actualObject = jsonDeserializer.convert(json, Customer.class);

        assertEquals(expectedObject.getId(), actualObject.getId());
        assertEquals(expectedObject.getFirstName(), actualObject.getFirstName());
        assertEquals(expectedObject.getLastName(), actualObject.getLastName());
        assertEquals(expectedObject.getDateBirth(), actualObject.getDateBirth());
        assertEquals(expectedObject.getOrders().size(), actualObject.getOrders().size());
    }

    @Test
    void convertShouldReturnExpectedOrderObject() throws JsonProcessingException {
        var expectedObject = OrderTestData.builder().build().buildOrder();
        var json = objectMapper.writeValueAsString(expectedObject);

        var actualObject = jsonDeserializer.convert(json, Order.class);

        assertEquals(expectedObject.getId(), actualObject.getId());
        assertEquals(expectedObject.getCreateDate(), actualObject.getCreateDate());
        assertEquals(expectedObject.getProducts().size(), actualObject.getProducts().size());
    }

    @Test
    void convertShouldReturnExpectedTestModelObject() throws JsonProcessingException {
        var expectedObject = TestModelTestData.builder().build().buildTestModel();
        var json = objectMapper.writeValueAsString(expectedObject);

        var actualObject = jsonDeserializer.convert(json, TestModel.class);

        assertEquals(expectedObject.getByteField(), actualObject.getByteField());
        assertEquals(expectedObject.getFloatField(), actualObject.getFloatField());
        assertEquals(expectedObject.getIntField(), actualObject.getIntField());
        assertEquals(expectedObject.getIntegerField(), actualObject.getIntegerField());
        assertEquals(expectedObject.getLongField(), actualObject.getLongField());
        assertEquals(expectedObject.getDoubleField(), actualObject.getDoubleField());
        assertEquals(expectedObject.getShortField(), actualObject.getShortField());
        assertEquals(expectedObject.getBigIntegerField(), actualObject.getBigIntegerField());
        assertTrue(expectedObject.getProduct().equals(actualObject.getProduct()));
        assertTrue(expectedObject.getCustomer().equals(actualObject.getCustomer()));
        assertEquals(expectedObject.getOrderList().size(), actualObject.getOrderList().size());
        assertEquals(expectedObject.getProductList().size(), actualObject.getProductList().size());
    }
}