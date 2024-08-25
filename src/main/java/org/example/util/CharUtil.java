package org.example.util;

import static org.example.constant.Constant.*;

/**
 * Утилиты для работы с символами в контексте JSON.
 */
public class CharUtil {

    /**
     * Проверяет, является ли строка "null".
     *
     * @param json строка для проверки.
     * @return true, если строка равна "null", иначе false.
     */
    public static boolean isNull(String json) {
        return NULL.equals(json);
    }

    /**
     * Проверяет, является ли символ представителем логического значения (true/false).
     *
     * @param ch символ для проверки.
     * @return true, если символ равен 't' или 'f', иначе false.
     */
    public static boolean isBoolean(char ch) {
        return ch == 't' || ch == 'f';
    }

    /**
     * Проверяет, является ли символ цифрой или знаком минус.
     *
     * @param ch символ для проверки.
     * @return true, если символ - цифра (0-9) или '-' (знак минус), иначе false.
     */
    public static boolean isNumber(char ch) {
        return (ch >= '0' && ch <= '9') || ch == '-';
    }

    /**
     * Проверяет, является ли символ началом строки (двойная кавычка).
     *
     * @param ch символ для проверки.
     * @return true, если символ равен '"', иначе false.
     */
    public static boolean isString(char ch) {
        return ch == '"';
    }

    /**
     * Проверяет, является ли символ началом массива (открывающая квадратная скобка).
     *
     * @param ch символ для проверки.
     * @return true, если символ равен '[', иначе false.
     */
    public static boolean isArray(char ch) {
        return ch == LEFT_BRACKET;
    }

    /**
     * Проверяет, является ли символ началом объекта (открывающая фигурная скобка).
     *
     * @param ch символ для проверки.
     * @return true, если символ равен '{', иначе false.
     */
    public static boolean isObject(char ch) {
        return ch == LEFT_CURLY_BRACE;
    }
}