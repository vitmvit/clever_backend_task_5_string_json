package org.example.util;

import org.example.annotation.JsonField;

import java.lang.reflect.Field;

public class JsonFieldUtil {

    /**
     * Получает имя поля, учитывая аннотацию JsonField.
     *
     * @param field поле, для которого нужно получить имя
     * @return имя поля
     */
    public static String getFieldName(Field field) {
        if (field.isAnnotationPresent(JsonField.class)) {
            return field.getAnnotation(JsonField.class).value();
        }
        return field.getName();
    }
}
