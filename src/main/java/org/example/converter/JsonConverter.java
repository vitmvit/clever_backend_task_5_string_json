package org.example.converter;

public interface JsonConverter {
    /**
     * преобразование объекта в строку (json)
     */
    String convert(Object object);

    /**
     * преобразование строки (json) в объект
     */
    <T> T convert(String json, Class<T> clazz);
}
