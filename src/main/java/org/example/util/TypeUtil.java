package org.example.util;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;

/**
 * Утилиты для работы с типами данных.
 */
public class TypeUtil {

    /**
     * Проверяет, является ли тип полем логическим (boolean).
     *
     * @param type класс, представляющий тип
     * @return true, если тип - Boolean, иначе false
     */
    public static boolean isBooleanType(Class<?> type) {
        return type == Boolean.TYPE || type == Boolean.class;
    }

    /**
     * Проверяет, является ли тип полем числовым.
     *
     * @param type класс, представляющий тип
     * @return true, если тип - числовой (включая примитивные типы), иначе false
     */
    public static boolean isNumberType(Class<?> type) {
        // Проверяем, является ли тип примитивным или наследуется от java.lang.Number
        return type.isPrimitive() ||
                Number.class.isAssignableFrom(type);
    }

    /**
     * Проверяет, является ли тип полем коллекцией.
     *
     * @param type класс, представляющий тип
     * @return true, если тип - коллекция, иначе false
     */
    public static boolean isCollectionType(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    /**
     * Проверяет, является ли тип полем картой.
     *
     * @param type класс, представляющий тип
     * @return true, если тип - карта, иначе false
     */
    public static boolean isMapType(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    /**
     * Проверяет, является ли тип полем OffsetDateTime.
     *
     * @param type класс, представляющий тип
     * @return true, если тип - OffsetDateTime, иначе false
     */
    public static boolean isLocalDate(Class<?> type) {
        return type.equals(OffsetDateTime.class);
    }
}