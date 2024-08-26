package org.example.converter.impl;

import org.example.constant.Constant;
import org.example.converter.JsonDeserializer;
import org.example.exception.JsonDeserializationException;
import org.example.parser.ParserType;
import org.example.parser.impl.ParserTypeImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static org.example.constant.Constant.*;
import static org.example.util.CharUtil.*;
import static org.example.util.JsonFieldUtil.getFieldName;
import static org.example.util.StringUtil.isEmpty;

/**
 * Реализация интерфейса {@link JsonDeserializer}, предоставляющая функциональность для десериализации JSON-строк
 * в объекты Java.
 */
public class JsonDeserializerImpl implements JsonDeserializer {

    /**
     * Преобразует строку JSON в экземпляр указанного класса.
     * <p>
     * Этот метод разбирает предоставленную строку JSON в карту, создает новый экземпляр
     * указанного класса и заполняет его поля значениями, извлеченными из карты JSON.
     * Если любой ключ в JSON не соответствует полю класса, будет выброшено исключение
     * {@link JsonDeserializationException}.
     * </p>
     *
     * @param json  строка JSON, которая будет преобразована в объект.
     * @param clazz класс создаваемого объекта.
     * @return экземпляр указанного класса, заполненный значениями из строки JSON.
     * @throws JsonDeserializationException если существует поле в JSON, которое не может быть установлено
     *                                      в экземпляре класса.
     */
    @Override
    public Object convert(String json, Class<?> clazz) {
        Map<String, String> jsonMap = jsonToMapConvert(json);
        Object object = createInstance(clazz);
        ParserType parser = new ParserTypeImpl(this);
        jsonMap.forEach((key, value) -> {
            try {
                setFieldValue(clazz, object, key, value, parser);
            } catch (Exception e) {
                throw new JsonDeserializationException(FIELD_NOT_FOUND_MESSAGE + key);
            }
        });
        return object;
    }

    /**
     * Извлекает массив из строки JSON.
     *
     * <p>
     * Метод проходит по символам строки JSON и использует стек для отслеживания открывающих
     * и закрывающих квадратных скобок. Если встречается первая открывающая скобка
     * ({@code '['}), запоминается её индекс. Когда закрывающая скобка
     * ({@code ']'}), соответствующая первой, найдена, возвращается подстрока массива.
     * В случае отсутствия корректных скобок выбрасывается
     * {@link JsonDeserializationException}.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь массив.
     * @return строка, представляющая массив JSON.
     * @throws JsonDeserializationException если в строке не найден правильный
     *                                      массив JSON или синтаксис JSON недопустим.
     */
    private static String getArray(String json) {
        int start = -1;
        int openBrackets = 0;
        for (int i = 0; i < json.length(); i++) {
            char currentChar = json.charAt(i);
            if (currentChar == LEFT_BRACKET) {
                if (openBrackets == 0) {
                    start = i;
                }
                openBrackets++;
            } else if (currentChar == RIGHT_BRACKET) {
                openBrackets--;
                if (openBrackets == 0) {
                    return json.substring(start, i + 1);
                }
            }
        }
        throw new JsonDeserializationException(INCORRECT_JSON_MESSAGE);
    }

    /**
     * Устанавливает значение указанного поля объекта, преобразуя входное значение с помощью парсера.
     *
     * <p>
     * Этот метод пытается найти поле с заданным именем в указанном классе, делает его доступным,
     * преобразует переданное значение с помощью предоставленного парсера и устанавливает его в поле
     * указанного объекта. Если поле не найдено или возникает ошибка доступа, выбрасывается
     * {@link JsonDeserializationException}.
     * </p>
     *
     * @param clazz  класс объекта, полю которого будет установлено значение.
     * @param object экземпляр объекта, в котором будет установлено поле.
     * @param key    имя поля, значение которого нужно установить (может содержать кавычки).
     * @param value  значение, которое будет преобразовано и установлено в поле.
     * @param parser парсер для преобразования значения в соответствующий тип для поля.
     * @throws JsonDeserializationException если происходит ошибка доступа к полю или если поле не найдено.
     */
    private void setFieldValue(Class<?> clazz, Object object, String key, String value, ParserType parser) {
        boolean fieldFound = false;
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = getFieldName(field);
            if (fieldName.equals(key.replace("\"", ""))) {
                try {
                    field.setAccessible(true);
                    Object parsedObject = parser.getObject(value, field);
                    field.set(object, parsedObject);
                    fieldFound = true;
                    break;
                } catch (IllegalAccessException e) {
                    throw new JsonDeserializationException(e.getMessage());
                }
            }
        }
        if (!fieldFound) {
            throw new JsonDeserializationException(FIELD_NOT_FOUND_FOR_KEY_MESSAGE + key);
        }
    }

    /**
     * Преобразует строку JSON в карту, где ключи и значения представлены в виде строк.
     *
     * <p>
     * Этот метод разбирает входящую строку JSON, извлекает пары ключ-значение и добавляет их в
     * {@link Map}. Возвращает карту с ключами и значениями на основе указанного JSON.
     * </p>
     *
     * @param inputJson входная строка JSON для преобразования.
     * @return {@link Map} с парами ключ-значение, извлеченными из JSON.
     */
    private Map<String, String> jsonToMapConvert(String inputJson) {
        String json = checkAndTrimJson(inputJson);
        Map<String, String> keyValueFields = new LinkedHashMap<>();
        while (!isEmpty(json)) {
            String key = getKey(json);
            json = json.substring(key.length() + 1);
            String value = extractValue(json);
            json = advanceJsonByValueLength(json, value);
            keyValueFields.put(createNameField(key), createValueField(value));
        }
        return keyValueFields;
    }

    /**
     * Проверяет и удаляет фигурные скобки из переданной строки JSON, если это необходимо.
     *
     * <p>
     * Если переданный JSON начинается с числа, метод возвращает его без изменений. Если он
     * начинается с открывающей фигурной скобки, то убирается первая и последняя скобки.
     * В противном случае выбрасывается {@link JsonDeserializationException}.
     * </p>
     *
     * @param json строка JSON для проверки и обрезки.
     * @return проверенная и обрезанная строка JSON.
     * @throws JsonDeserializationException если формат JSON некорректен.
     */
    private String checkAndTrimJson(String json) {
        if (isNumber(json.charAt(0))) return json;
        if (json.charAt(0) == LEFT_CURLY_BRACE) return json.substring(1, json.length() - 1);
        throw new JsonDeserializationException(INCORRECT_JSON_MESSAGE + ": " + json);
    }

    /**
     * Извлекает значение из переданной строки JSON на основе первого символа.
     *
     * <p>
     * Метод определяет тип значения по первому символу строки JSON и вызывает соответствующий
     * метод для извлечения значения (логическое, объектное, числовое, строковое или массив).
     * Если переданная строка равна null, возвращается строка NULL. Если значение не удается извлечь,
     * выбрасывается {@link JsonDeserializationException}.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь значение.
     * @return извлеченное значение из JSON в виде строки.
     * @throws JsonDeserializationException если не удается извлечь значение.
     */
    private String extractValue(String json) {
        if (isNull(json)) return NULL;
        else if (isBoolean(json.charAt(0))) return getBoolean(json);
        else if (isObject(json.charAt(0))) return getObject(json);
        else if (isNumber(json.charAt(0))) return getNumber(json);
        else if (isString(json.charAt(0))) return getString(json);
        else if (isArray(json.charAt(0))) return getArray(json);
        throw new JsonDeserializationException(EXTRACT_VALUE_ERROR_MESSAGE + json);
    }

    /**
     * Перемещает указатель JSON на символы, следующие за извлеченным значением.
     *
     * <p>
     * Метод возвращает оставшуюся строку JSON, начиная с позиции, следующей за длиной значения
     * и разделителем (например, запятой или пробелом). Если длина строки JSON меньше или равна
     * длине значения, возвращается пустая строка.
     * </p>
     *
     * @param json  строка JSON, из которой необходимо продвинуть курсор.
     * @param value извлеченное значение для определения новой позиции.
     * @return оставшаяся строка JSON после извлеченного значения.
     */
    private String advanceJsonByValueLength(String json, String value) {
        int valueLength = value.length() + 1;
        return (json.length() <= valueLength) ? "" : json.substring(valueLength);
    }

    /**
     * Возвращает значение колюча без двойных кавычек.
     *
     * <p>
     * Метод создает новую строку, в которой все символы двойных кавычек (" ")
     * заменяются на пустую строку.
     * </p>
     *
     * @param line строка, из которой необходимо удалить двойные кавычки.
     * @return Значение ключа без двойных кавычек.
     */
    private String createNameField(String line) {
        return line.replace("\"", "");
    }

    /**
     * Возвращает значение value для map без двойных кавычек.
     *
     * <p>
     * Метод проверяет, начинается ли строка с двойной кавычки и заканчивается
     * на двойную кавычку. Также он проверяет, что строка не содержит фигурные скобки
     * или квадратные скобки. Если все условия выполнены, метод возвращает новую строку,
     * где двойные кавычки удалены. В противном случае возвращается оригинальная строка.
     * </p>
     *
     * @param line строка, из которой необходимо извлечь значение.
     * @return строка без двойных кавычек, если она удовлетворяет условиям,
     * иначе оригинальная строка.
     */
    private String createValueField(String line) {
        if (line.startsWith("\"") && line.endsWith("\"") && !line.contains(String.valueOf(LEFT_CURLY_BRACE)) && !line.contains(String.valueOf(LEFT_BRACKET))) {
            return line.replace("\"", "");
        }
        return line;
    }

    /**
     * Извлекает ключ из строки JSON до второго вхождения двойной кавычки.
     *
     * <p>
     * Метод проходит по символам переданной строки JSON и собирает их в
     * {@link StringBuilder} до тех пор, пока не встретит два символа двойной
     * кавычки. Если строка не содержит двух кавычек, будет возвращена
     * вся строка.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь ключ.
     * @return строка, содержащая ключ до второго вхождения двойной кавычки или
     * пустая строка, если входная строка пуста.
     */
    private String getKey(String json) {
        StringBuilder keyBuilder = new StringBuilder();
        int quoteCount = 0;
        for (char c : json.toCharArray()) {
            keyBuilder.append(c);
            if (c == '"') quoteCount++;
            if (quoteCount == 2) break;
        }
        return keyBuilder.toString();
    }

    /**
     * Извлекает булевое значение из строки JSON.
     *
     * <p>
     * Метод проверяет, начинается ли переданная строка JSON с "true" или "false".
     * Если строка начинается с "true", метод возвращает строку "true"; если
     * начинается с "false", возвращает строку "false". Если строка не начинается
     * с ни одного из этих значений, выбрасывается {@link JsonDeserializationException}.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь булевое значение.
     * @return строка "true" или "false" в зависимости от содержимого строки.
     * @throws JsonDeserializationException если строка не начинается с "true" или "false".
     */
    private String getBoolean(String json) {
        if (json.startsWith(TRUE_STRING)) return TRUE_STRING;
        else if (json.startsWith(FALSE_STRING)) return FALSE_STRING;
        throw new JsonDeserializationException(INCORRECT_JSON_MESSAGE);
    }

    /**
     * Извлекает числовое значение из строки JSON.
     *
     * <p>
     * Метод проверяет, является ли переданная строка JSON пустой или null.
     * Если это так, выбрасывается {@link JsonDeserializationException} с сообщением
     * об ошибке. Затем он проходит по символам строки, пока они являются
     * цифрами или символом точки (.), извлекая часть строки, представляющую число.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь числовое значение.
     * @return строка, представляющая числовое значение.
     * @throws JsonDeserializationException если строка пустая, null или не содержит допустимого числа.
     */
    private String getNumber(String json) {
        if (isEmpty(json)) throw new JsonDeserializationException(INCORRECT_JSON_MESSAGE);
        int i = 0;
        while (i < json.length() && (Character.isDigit(json.charAt(i)) || json.charAt(i) == '.')) {
            i++;
        }
        return json.substring(0, i);
    }

    /**
     * Извлекает объект из строки JSON.
     *
     * <p>
     * Метод проверяет, является ли переданная строка JSON пустой. Если она пустая,
     * возвращается специальное значение {@code NULL}. Если строка не начинается с
     * символа открывающей фигурной скобки ({@code '{'), выбрасывается
     * {@link JsonDeserializationException} с информацией об ошибке.
     * Далее метод проходит по символам строки, подсчитывая количество открывающих
     * и закрывающих фигурных скобок, и извлекает подстроку, представляющую объект,
     * до тех пор, пока не найдется соответствующая закрывающая скобка.
     * Если закрывающая скобка не найдена, выбрасывается исключение с описанием проблемы.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь объект.
     * @return строка, представляющая объект JSON.
     * @throws JsonDeserializationException если строка не начинается с '{',
     *                                      отсутствует соответствующая закрывающая фигурная скобка
     *                                      или синтаксис JSON недопустим.
     */
    private String getObject(String json) {
        if (isEmpty(json)) return NULL;
        if (isEmpty(json) || !json.startsWith(String.valueOf(LEFT_CURLY_BRACE))) {
            throw new JsonDeserializationException(INCORRECT_JSON_MESSAGE + ": " + json);
        }
        StringBuilder objectBuilder = new StringBuilder();
        int braceCounter = 0;
        for (char c : json.toCharArray()) {
            objectBuilder.append(c);
            if (c == LEFT_CURLY_BRACE) braceCounter++;
            if (c == RIGHT_CURLY_BRACE) braceCounter--;
            if (braceCounter == 0) break;
        }
        return objectBuilder.toString();
    }

    /**
     * Извлекает строковое значение из строки JSON.
     *
     * <p>
     * Метод сначала пытается получить строковое значение с помощью метода
     * {@link #getStringValue(String)}. Если возникает {@link JsonDeserializationException},
     * он пытается извлечь символьное значение с помощью метода
     * {@link #getCharacterValue(String)}.
     * Если оба метода не могут извлечь значение, будет выброшено
     * {@link JsonDeserializationException}.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь строковое значение.
     * @return строковое значение, извлеченное из строки JSON.
     * @throws JsonDeserializationException если ни один из методов не может извлечь значение.
     */
    private String getString(String json) {
        try {
            return getStringValue(json);
        } catch (JsonDeserializationException e) {
            return getCharacterValue(json);
        }
    }

    /**
     * Извлекает значение символа из строки JSON.
     *
     * <p>
     * Метод ищет символы в строке JSON, используя регулярное выражение.
     * Если символ найден, он возвращается. В противном случае
     * выбрасывается {@link JsonDeserializationException}.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь символ.
     * @return найденный символ в виде строки.
     * @throws JsonDeserializationException если символ не найден в строке JSON.
     */
    private String getCharacterValue(String json) {
        return Pattern.compile(CHAR_PATTERN).matcher(json)
                .results()
                .map(MatchResult::group)
                .findAny()
                .orElseThrow(() -> new JsonDeserializationException(INCORRECT_JSON_MESSAGE));
    }

    /**
     * Извлекает строковое значение из строки JSON.
     *
     * <p>
     * Метод проверяет, не является ли входная строка null или пустой.
     * Если строка не пустая, используются регулярные выражения для поиска
     * строкового значения. В случае успеха возвращается найденное значение,
     * иначе выбрасывается {@link JsonDeserializationException}.
     * </p>
     *
     * @param json строка JSON, из которой необходимо извлечь строковое значение.
     * @return найденное строковое значение.
     * @throws JsonDeserializationException если строка пустая или значение не найдено.
     */
    private String getStringValue(String json) {
        if (isEmpty(json)) throw new JsonDeserializationException(NULL_VALUE_MESSAGE);
        return Pattern.compile(Constant.STRING_PATTERN).matcher(json)
                .results()
                .map(MatchResult::group)
                .findFirst()
                .orElseThrow(() -> new JsonDeserializationException(INCORRECT_JSON_MESSAGE + ": " + json));
    }

    /**
     * Создает новый экземпляр указанного класса.
     *
     * <p>
     * Метод вызывает конструктор по умолчанию указанного класса, устанавливая доступ к
     * конструктору, если он является частным или защищенным. В случае ошибки
     * выбрасывается {@link JsonDeserializationException}.
     * </p>
     *
     * @param clazz класс, для которого нужно создать экземпляр.
     * @return созданный экземпляр класса.
     * @throws JsonDeserializationException если не удается инициализировать объект
     *                                      из-за проблем с доступом к конструктору.
     */
    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new JsonDeserializationException(INITIALIZE_ERROR_MESSAGE + ": " + e);
        }
    }
}