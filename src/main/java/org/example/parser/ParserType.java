package org.example.parser;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.stream.Stream;

public interface ParserType {

    Object getObject(String value, Field declaredField);

    Object getNumber(String value, Class<?> type);

    UUID getUUID(String value);

    LocalDate getLocalDate(String value);

    OffsetDateTime getOffsetDateTime(String value);

    LinkedHashMap<Object, String> getMap(String value, Field declaredField);

    LinkedHashMap<Object, String> getSimpleMap(String value, Field declaredField);

    LinkedHashMap<Object, String> getNestedMap(String value, Field declaredField);

    Collection<Object> getCollection(String value, Field declaredField);

    Stream<Object> getCollectionStream(String value, Field declaredField);

    Object getGeneric(Field declaredField, String str);

    Enum<?> getEnum(String value, Field declaredField);
}