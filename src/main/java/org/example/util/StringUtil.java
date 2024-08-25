package org.example.util;

public class StringUtil {

    /**
     * Проверяет, является ли строка пустой (null или пустая).
     *
     * @param line строка для проверки
     * @return true, если строка пустая, иначе false
     */
    public static boolean isEmpty(CharSequence line) {
        return line == null || line.isEmpty();
    }

    /**
     * Проверяет, является ли строка не пустой.
     *
     * @param line строка для проверки
     * @return true, если строка не пустая, иначе false
     */
    public boolean isNotEmpty(CharSequence line) {
        return !isEmpty(line);
    }
}
