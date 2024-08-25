package org.example.converter.impl;

import org.example.converter.JsonSerializer;
import org.example.exception.JsonSerializationException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.example.constant.Constant.*;
import static org.example.util.JsonFieldUtil.getFieldName;

/**
 * Реализация интерфейса {@link JsonSerializer}, предоставляющая функциональность для сериализации объектов в формат JSON.
 */
public class JsonSerializerImpl implements JsonSerializer {

    private final StringBuilder json = new StringBuilder();

    /**
     * Преобразует заданный объект в строку формата JSON.
     *
     * @param object объект, который требуется сериализовать
     * @return строка, представляющая объект в формате JSON
     * @throws JsonSerializationException если происходит ошибка доступа к полям объекта
     */
    @Override
    public String convert(Object object) {
        if (object == null) {
            return String.valueOf(LEFT_CURLY_BRACE + RIGHT_CURLY_BRACE);
        }
        json.append(LEFT_CURLY_BRACE);
        for (var field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                String fieldName = getFieldName(field);
                buildBody(fieldName, field.get(object));
            } catch (Exception ex) {
                throw new JsonSerializationException(ERROR_ACCESSING_FIELDS_MESSAGE + ex.getMessage());
            }
        }
        if (json.length() > 1) {
            json.setLength(json.length() - 1);
        }
        json.append(RIGHT_CURLY_BRACE);
        return json.toString();
    }

    /**
     * Строит содержимое JSON для заданного поля и его значения.
     *
     * @param fieldName  имя поля
     * @param fieldValue значение поля
     */
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

    /**
     * Строит JSON-массив для заданного списка.
     *
     * @param list список объектов, который будет сериализован в JSON массив
     */
    private void buildList(List<?> list) {
        json.append(LEFT_BRACKET);
        for (Object object : list) {
            convert(object);
            json.append(",");
        }
        json.setLength(json.length() - 1);
        json.append(RIGHT_BRACKET);
    }

    /**
     * Проверяет, содержит ли объект геттер.
     *
     * @param object объект для проверки
     * @return true, если метод геттера существует, иначе false
     * @throws JsonSerializationException если метод геттера не найден
     */
    private boolean findOutGetter(Object object) {
        try {
            Field[] fieldArray = object.getClass().getDeclaredFields();
            if (fieldArray.length > 0) {
                String fieldName = fieldArray[0].getName();
                String methodName = PREFIX_GET + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                object.getClass().getDeclaredMethod(methodName);
                return true;
            }
        } catch (Exception ex) {
            throw new JsonSerializationException(GETTER_METHOD_NOT_FOUND_MESSAGE + ex.getMessage());
        }
        return false;
    }
}
