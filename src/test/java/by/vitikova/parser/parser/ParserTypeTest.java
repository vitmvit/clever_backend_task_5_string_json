package by.vitikova.parser.parser;

import by.vitikova.parser.converter.impl.JsonDeserializerImpl;
import by.vitikova.parser.exception.JsonParseException;
import by.vitikova.parser.parser.impl.ParserTypeImpl;
import by.vitikova.parser.util.CustomerTestData;
import by.vitikova.parser.util.TestModelTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static by.vitikova.parser.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTypeTest {

    private ParserType parserType;

    @BeforeEach
    public void setup() {
        parserType = new ParserTypeImpl(new JsonDeserializerImpl());
    }

    @Test
    void getObjectShouldReturnBoolean() throws NoSuchFieldException {
        var field = TestModelTestData.class.getDeclaredField("booleanField");
        assertEquals(Boolean.TRUE, parserType.getObject(TRUE_STRING, field));
    }

    @Test
    void getObjectShouldReturnInteger() throws NoSuchFieldException {
        var field = TestModelTestData.class.getDeclaredField("intField");
        assertEquals(123, parserType.getObject("123", field));
    }

    @Test
    void getObjectShouldReturnDouble() throws NoSuchFieldException {
        var field = TestModelTestData.class.getDeclaredField("doubleField");
        assertEquals(123.45, parserType.getObject("123.45", field));
    }

    @Test
    void getObjectShouldReturnString() throws NoSuchFieldException {
        var field = CustomerTestData.class.getDeclaredField("firstName");
        assertEquals("name", parserType.getObject("name", field));
    }

    @Test
    void getObjectShouldReturnLocalDate() throws NoSuchFieldException {
        var field = CustomerTestData.class.getDeclaredField("dateBirth");
        assertEquals(LocalDate.of(2021, 1, 1), parserType.getObject("2021-01-01", field));
    }

    @Test
    void getObjectShouldReturnJsonParseExceptionWhereInvalidLocalDate() throws NoSuchFieldException {
        var field = CustomerTestData.class.getDeclaredField("dateBirth");
        var invalidDate = "2021-31-31";

        var exception = assertThrows(JsonParseException.class, () -> parserType.getObject(invalidDate, field));

        assertTrue(exception.getMessage().contains(INVALID_DATE_STRING_MESSAGE + invalidDate));
    }

    @Test
    void getObjectShouldReturnUuid() throws NoSuchFieldException {
        var field = CustomerTestData.class.getDeclaredField("id");
        var expectedUUID = CustomerTestData.builder().build().getId();

        assertEquals(expectedUUID, parserType.getObject(String.valueOf(expectedUUID), field));
    }

    @Test
    void getObjectShouldReturnJsonParseExceptionWhereInvalidUuid() throws NoSuchFieldException {
        var field = CustomerTestData.class.getDeclaredField("id");
        var invalidUUID = "invalid-uuid";

        var exception = assertThrows(JsonParseException.class, () -> parserType.getObject(invalidUUID, field));

        assertTrue(exception.getMessage().contains(INVALID_UUID_STRING_MESSAGE + invalidUUID));
    }

    @Test
    void getObjectShouldReturnMap() throws NoSuchFieldException {
        var field = TestModelTestData.class.getDeclaredField("map");
        var mapString = "{\"key1\":\"value1\",\"key2\":\"value2\"}";

        var expectedMap = TestModelTestData.buildMap();

        assertEquals(expectedMap, parserType.getObject(mapString, field));
    }
}