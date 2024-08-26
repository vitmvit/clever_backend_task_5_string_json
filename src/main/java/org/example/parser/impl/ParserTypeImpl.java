package org.example.parser.impl;

import lombok.AllArgsConstructor;
import org.example.converter.JsonDeserializer;
import org.example.exception.JsonParseException;
import org.example.parser.ParserType;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.constant.Constant.*;
import static org.example.util.TypeUtil.*;

@AllArgsConstructor
public class ParserTypeImpl implements ParserType {

    private final JsonDeserializer jsonDeserializer;

    /**
     * Извлекает объект из переданного значения на основе типа поля, определенного в {@code declaredField}.
     * <p>
     * Метод определяет тип поля и возвращает соответствующий объект на основе переданного
     * значения {@code value}. Поддерживаются различные типы данных, такие как логические,
     * числовые значения, строки, коллекции, карты, перечисления, массивы, UUID и типы
     * даты/времени.
     *
     * @param value         строковое значение, представляющее данные в JSON, которые необходимо преобразовать.
     * @param declaredField поле класса, для которого необходимо определить тип и извлечь значение.
     * @return объект соответствующего типа, извлеченный из значения {@code value}.
     */
    public Object getObject(String value, Field declaredField) {
        Class<?> type = declaredField.getType();
        if (isBooleanType(type)) {
            return Boolean.valueOf(value);
        } else if (isNumberType(type)) {
            return getNumber(value, type);
        } else if (type.equals(String.class)) {
            return value;
        } else if (isCollectionType(type)) {
            return getCollection(value, declaredField);
        } else if (isMapType(type)) {
            return getMap(value, declaredField);
        } else if (type.isEnum()) {
            return getEnum(value, declaredField);
        } else if (type.isArray()) {
            return getArray(value, declaredField);
        } else if (type.equals(UUID.class)) {
            return getUUID(value);
        } else if (type.equals(LocalDate.class)) {
            return getLocalDate(value);
        } else if (isLocalDate(type)) {
            return getOffsetDateTime(value);
        } else {
            return jsonDeserializer.convert(value, type);
        }
    }

    /**
     * Преобразует строковое значение в число заданного типа.
     *
     * @param value строковое значение, представляющее число.
     * @param type  класс, определяющий тип числового значения для преобразования.
     * @return число соответствующего типа, извлеченное из строки {@code value}.
     * @throws NumberFormatException если значение не может быть преобразовано в указанный числовой тип.
     */
    public Object getNumber(String value, Class<?> type) {
        return switch (type.getSimpleName()) {
            case INTEGER_WRAPPER, INT_PRIMITIVE -> Integer.parseInt(value);
            case LONG_WRAPPER, LONG_PRIMITIVE -> Long.parseLong(value);
            case BIG_DECIMAL_WRAPPER -> new BigDecimal(value);
            case BIG_INTEGER_WRAPPER -> new BigInteger(value);
            case BYTE_WRAPPER, BYTE_PRIMITIVE -> Byte.parseByte(value);
            case SHORT_WRAPPER, SHORT_PRIMITIVE -> Short.parseShort(value);
            case FLOAT_WRAPPER, FLOAT_PRIMITIVE -> Float.parseFloat(value);
            default -> Double.parseDouble(value);
        };
    }

    /**
     * Преобразует строковое значение в объект UUID.
     *
     * @param value строковое представление UUID.
     * @return объект UUID, созданный из строки {@code value}.
     * @throws IllegalArgumentException если {@code value} не является допустимым UUID.
     */
    public UUID getUUID(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException(INVALID_UUID_STRING_MESSAGE + value);
        }
    }

    /**
     * Преобразует строковое значение в объект LocalDate.
     *
     * @param value строковое представление даты.
     * @return объект LocalDate, созданный из строки {@code value}.
     * @throws JsonParseException если {@code value} не соответствует формату даты.
     */
    public LocalDate getLocalDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            throw new JsonParseException(INVALID_DATE_STRING_MESSAGE + value);
        }
    }

    /**
     * Преобразует строковое значение в объект OffsetDateTime.
     *
     * @param value строковое представление даты и времени со сдвигом.
     * @return объект OffsetDateTime, созданный из строки {@code value}.
     * @throws JsonParseException если {@code value} не соответствует формату даты и времени.
     */
    public OffsetDateTime getOffsetDateTime(String value) {
        try {
            return OffsetDateTime.parse(value);
        } catch (Exception e) {
            throw new JsonParseException(INVALID_OFFSET_DATETIME_STRING_MESSAGE + value);
        }
    }

    /**
     * Преобразует строковое значение в карту.
     *
     * @param value         строковое представление карты.
     * @param declaredField поле класса, представляющее карту.
     * @return LinkedHashMap, созданная из строки.
     */
    public LinkedHashMap<Object, String> getMap(String value, Field declaredField) {
        if (value.matches(MAP_PATTERN)) {
            return getNestedMap(value, declaredField);
        } else {
            return getSimpleMap(value, declaredField);
        }
    }

    /**
     * Преобразует строку в простую карту.
     *
     * @param value         строковое представление карты.
     * @param declaredField поле класса.
     * @return LinkedHashMap, созданная из строки.
     */
    public LinkedHashMap<Object, String> getSimpleMap(String value, Field declaredField) {
        return Arrays.stream(value.split("\\n"))
                .map(this::cleanEntry)
                .flatMap(Arrays::stream)
                .map(s -> s.split(COLON_STRING))
                .collect(Collectors.toMap(
                        strings -> getGeneric(declaredField, strings[0]),
                        strings -> strings[1],
                        (v1, v2) -> v2,
                        LinkedHashMap::new
                ));
    }

    /**
     * Преобразует строку во вложенную карту.
     *
     * @param value         строковое представление вложенной карты.
     * @param declaredField поле класса.
     * @return LinkedHashMap, созданная из строки.
     */
    public LinkedHashMap<Object, String> getNestedMap(String value, Field declaredField) {
        return value.lines()
                .map(s -> s.substring(1, s.length() - 1))
                .map(s -> s.split(COLON_STRING, 2))
                .collect(Collectors.toMap(
                        strings -> getGeneric(declaredField, strings[0].replace("\"", "")),
                        strings -> strings[1],
                        (v1, v2) -> v2,
                        LinkedHashMap::new
                ));
    }

    /**
     * Преобразует строку в коллекцию.
     *
     * @param value         строковое представление коллекции.
     * @param declaredField поле класса, представляющее коллекцию.
     * @return коллекция, созданная из строки.
     */
    public Collection<Object> getCollection(String value, Field declaredField) {
        Class<?> type = declaredField.getType();
        return switch (type.getName()) {
            case LIST_TYPE -> getCollectionStream(value, declaredField).toList();
            case SET_TYPE -> getCollectionStream(value, declaredField).collect(Collectors.toSet());
            default -> Collections.emptyList();
        };
    }

    /**
     * Получает stream объектов из строки.
     *
     * @param value         строковое представление коллекции.
     * @param declaredField поле класса, представляющее коллекцию.
     * @return поток объектов, полученных из строки.
     */
    public Stream<Object> getCollectionStream(String value, Field declaredField) {
        return Arrays.stream(value.split("\\n"))
                .map(s -> s.replace(String.valueOf(LEFT_BRACKET), "")
                        .replace(String.valueOf(RIGHT_BRACKET), "")
                        .trim())
                .flatMap(s -> s.startsWith(String.valueOf(LEFT_CURLY_BRACE)) ?
                        Arrays.stream(s.split(COLLECTION_SPLIT_PATTERN)) :
                        Arrays.stream(s.split(",")))
                .map(s -> getGeneric(declaredField, s));
    }

    /**
     * Получает объект соответствующего типа из строки.
     *
     * @param declaredField поле класса, представляющее объект.
     * @param str           строковое значение.
     * @return преобразованный объект.
     */
    public Object getGeneric(Field declaredField, String str) {
        if (declaredField.getGenericType() instanceof ParameterizedType type) {
            Class<?> generic = (Class<?>) type.getActualTypeArguments()[0];
            if (Number.class.isAssignableFrom(generic)) {
                return getNumber(str, generic);
            } else if (!generic.getName().startsWith(PREFIX_JAVA)) {
                return jsonDeserializer.convert(str, generic);
            }
        }
        return str;
    }

    /**
     * Получает перечисление из строки.
     *
     * @param value         строковое представление перечисления.
     * @param declaredField поле класса, представляющее перечисление.
     * @return найденное перечисление или исключение, если не найдено.
     */
    public Enum<?> getEnum(String value, Field declaredField) {
        Class<?> enumClass = declaredField.getType();
        if (!enumClass.isEnum()) {
            throw new JsonParseException(NOT_ENUM_TYPE_MESSAGE);
        }
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(enumValue -> ((Enum<?>) enumValue).name().equals(value))
                .map(enumValue -> (Enum<?>) enumValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Получает массив объектов из строки.
     *
     * @param value         строковое представление массива.
     * @param declaredField поле класса, представляющее массив.
     * @return массив объектов.
     */
    private Object getArray(String value, Field declaredField) {
        Class<?> arrayType = declaredField.getType().getComponentType();
        String[] jsonArray = value.substring(1, value.length() - 1).split(SUBSTRING_ARRAY_PATTERN);
        Object array = Array.newInstance(arrayType, jsonArray.length);
        for (int i = 0; i < jsonArray.length; i++) {
            Array.set(array, i, jsonDeserializer.convert(jsonArray[i].trim(), arrayType));
        }
        return array;
    }

    /**
     * Убирает ненужные символы из строки.
     *
     * @param entry строка для обработки.
     * @return массив строк без лишних символов.
     */
    private String[] cleanEntry(String entry) {
        return entry.replace(String.valueOf(LEFT_CURLY_BRACE), "")
                .replace(String.valueOf(RIGHT_CURLY_BRACE), "")
                .replace("\"", "")
                .trim().split(",");
    }
}