package by.vitikova.parser.annotation;

import by.vitikova.parser.util.JsonFieldTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JsonFieldTest {

    private Class<JsonFieldTestBuilder> clazz;

    @BeforeEach
    public void setup() {
        clazz = JsonFieldTestBuilder.class;
    }

    @Test
    void jsonFieldShouldReturnTrueWhereAnnotationEnable() throws NoSuchFieldException {
        var field = clazz.getDeclaredField("name");
        var jsonField = field.getAnnotation(JsonField.class);

        assertEquals("firstName", jsonField.value());
    }

    @Test
    void jsonFieldShouldReturnNullWhereAnnotationDisable() throws NoSuchFieldException {
        var field = clazz.getDeclaredField("age");
        var jsonField = field.getAnnotation(JsonField.class);

        assertNull(jsonField);
    }
}