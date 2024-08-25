package org.example.constant;

public class Constant {

    // exception messages
    public static final String ERROR_ACCESSING_FIELDS_MESSAGE = "Error accessing object fields: ";
    public static final String GETTER_METHOD_NOT_FOUND_MESSAGE = "Getter method not found: ";
    public static final String FIELD_NOT_FOUND_MESSAGE = "Field not found: ";
    public static final String OBJECT_GENERATION_ERROR_MESSAGE = "Object generation error!";
    public static final String INCORRECT_JSON_MESSAGE = "Incorrect json format!";
    public static final String NULL_VALUE_MESSAGE = "Shouldn't be null!";
    public static final String EXTRACT_VALUE_ERROR_MESSAGE = "Extract value error from: ";
    public static final String INITIALIZE_ERROR_MESSAGE = "Initialize object error!";

    // structure
    public static final String PREFIX_GET = "get";
    public static final String PREFIX_JAVA = "java";
    public static final String NULL = "null";
    public static final char LEFT_BRACKET = '[';
    public static final char RIGHT_BRACKET = ']';
    public static final char LEFT_CURLY_BRACE = '{';
    public static final char RIGHT_CURLY_BRACE = '}';
    public static final String TRUE_STRING = "true";
    public static final String FALSE_STRING = "false";
    public static final String COLON_STRING = ":";

    // type
    public static final String INTEGER_WRAPPER = "Integer";
    public static final String LONG_WRAPPER = "Long";
    public static final String BIG_DECIMAL_WRAPPER = "BigDecimal";
    public static final String BIG_INTEGER_WRAPPER = "BigInteger";
    public static final String BYTE_WRAPPER = "Byte";
    public static final String SHORT_WRAPPER = "Short";
    public static final String FLOAT_WRAPPER = "Float";
    public static final String INT_PRIMITIVE = "int";
    public static final String LONG_PRIMITIVE = "long";
    public static final String BYTE_PRIMITIVE = "byte";
    public static final String SHORT_PRIMITIVE = "short";
    public static final String FLOAT_PRIMITIVE = "float";
    public static final String LIST_TYPE = "java.util.List";
    public static final String SET_TYPE = "java.util.Set";

    // pattern
    public static final String CHAR_PATTERN = "([0-9]{1,5}|\"\\\\[uU][0-9a-fA-F]{4}\"|\"?null\"?|\"\\\\[bfnrt\"]\"|\".\")";
    public static final String STRING_PATTERN = "\"([^\"]*(\"{2})?[^\"]*)*\"";
    public static final String SUBSTRING_ARRAY_PATTERN = ",(?=\\{)";
    public static final String COLLECTION_SPLIT_PATTERN = "(?<=\\}),(?=\\{)";
    public static final String MAP_PATTERN = ".*\\{.*\\{.*\\}.*\\}.*";

}
