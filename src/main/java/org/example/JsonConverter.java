package org.example;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class JsonConverter {

    private final StringBuilder json = new StringBuilder();

    /**
     * Конвертирует объект в json
     *
     * @param object - объект для конвертации
     * @return строку json
     */
    public String convert(Object object) {
        json.append("{");
        for (var field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                buildBody(field.getName(), field.get(object));
            } catch (IllegalAccessException ignored) {
            }
        }
        if (json.length() > 1) {
            json.setLength(json.length() - 1);
        }
        json.append("}");
        return json.toString();
    }

    private void buildBody(String fieldName, Object fieldValue) {
        if (fieldValue instanceof Collection) {
            json.append("\"").append(fieldName).append("\":");
            buildList((List<?>) fieldValue);
            json.append(",");
        } else if (fieldValue instanceof Serializable) {
            if (fieldValue instanceof Number) {
                json.append("\"").append(fieldName).append("\":").append(fieldValue).append(",");
            } else if (fieldValue instanceof Boolean) {
                json.append("\"").append(fieldName).append("\":").append(fieldValue).append(",");
            } else {
                json.append("\"").append(fieldName).append("\":\"").append(fieldValue).append("\",");
            }
        } else {
            if (findOutGetter(fieldValue)) {
                json.append("\"").append(fieldName).append("\":");
                convert(fieldValue);
                json.append(",");
            } else {
                json.append("\"").append(fieldName).append("\":\"").append(convert(fieldValue)).append("\",");
            }
        }
    }

    private void buildList(List<?> list) {
        json.append("[");
        for (Object object : list) {
            convert(object);
            json.append(",");
        }
        json.setLength(json.length() - 1);
        json.append("]");
    }

    private boolean findOutGetter(Object object) {
        try {
            Field[] fieldArray = object.getClass().getDeclaredFields();
            if (fieldArray.length > 0) {
                String fieldName = fieldArray[0].getName();
                String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                object.getClass().getDeclaredMethod(methodName);
                return true;
            }
        } catch (NoSuchMethodException ignored) {
        }
        return false;
    }
}