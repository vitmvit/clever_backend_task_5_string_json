package org.example;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class JsonConverter {

    private static final String FIELD_FORMAT = "\"%s\":";
    private final StringBuilder json = new StringBuilder();

    public String convert(Object object) {
        json.append("{");
        for (var field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                buildBody(field.getName(), field.get(object));
            } catch (IllegalAccessException ignored) {
            }
        }
        if (json.length() > 2) {
            json.setLength(json.length() - 2);
        }
        json.append("}");
        return json.toString();
    }

    private void buildBody(String fieldName, Object fieldValue) {
        if (fieldValue instanceof Collection) {
            json.append("\"").append(fieldName).append("\": ");
            buildList((List<?>) fieldValue);
            json.append(", ");
        } else if (fieldValue instanceof Serializable) {
            if (fieldValue instanceof Number) {
                json.append("\"").append(fieldName).append("\": ").append(fieldValue).append(", ");
            } else {
                json.append("\"").append(fieldName).append("\": \"").append(fieldValue).append("\", ");
            }
        } else {
            json.append("\"").append(fieldName).append("\": \"").append(convert(fieldValue)).append("\", ");
        }
    }

    private void buildList(List<?> list) {
        json.append("[");
        for (Object object : list) {
            convert(object);
            json.append(", ");
        }
        json.setLength(json.length() - 2);
        json.append("]");
    }
}