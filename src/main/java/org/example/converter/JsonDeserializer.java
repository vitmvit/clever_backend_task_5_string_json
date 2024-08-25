package org.example.converter;

public interface JsonDeserializer {

    Object convert(String json, Class<?> clazz);
}
