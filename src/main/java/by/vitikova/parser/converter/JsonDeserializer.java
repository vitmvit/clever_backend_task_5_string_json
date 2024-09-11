package by.vitikova.parser.converter;

public interface JsonDeserializer {

    <T> T convert(String json, Class<T> clazz);
}