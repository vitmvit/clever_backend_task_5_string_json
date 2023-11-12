package org.example.converter.impl;

import org.example.converter.JsonConverter;
import org.example.exception.JsonConverterException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

public class JsonConverterImpl implements JsonConverter {

    private static final String FIELD_FORMAT = "\"%s\":";

    private final StringBuilder json = new StringBuilder();
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private static Map<String, Object> buildMap(String json) {
        json = json.trim().replace("\n", "");
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        if (json.startsWith("{") && json.endsWith("}")) {
            String substring = json.substring(1, json.length() - 1);
            String[] partArray = substring.split(",");
            for (String pair : partArray) {
                String[] keyValueArray = pair.split(":");
                if (keyValueArray.length == 2) {
                    String key = keyValueArray[0].trim().replace("\"", "");
                    String value = keyValueArray[1].trim().replace("\"", "");
                    jsonMap.put(key, value);
                }
            }
        }
        return jsonMap;
    }

    /**
     * преобразование объекта в строку (json)
     */
    @Override
    public String convert(Object object) {
        if (object == null) {
            return "{}";
        }
        json.append("{");
        for (var field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                buildBody(field.getName(), field.get(object));
            } catch (IllegalAccessException ignored) {
            }
        }
        if (json.length() > 1) {
            json.setLength(json.length() - 1);
        }
        json.append("}");
        return json.toString();
    }

    /**
     * преобразование строки (json) в объект
     */
    @Override
    public <T> T convert(String json, Class<T> clazz) {
        if (isJson(json)) {
            return convert(buildMap(json), clazz);
        } else {
            throw new JsonConverterException("Incorrect JSON string");
        }
    }

    private void buildBody(String fieldName, Object fieldValue) {
        if (fieldValue instanceof Collection) {
            json.append("\"").append(fieldName).append("\":");
            buildList((List<?>) fieldValue);
            json.append(",");
        } else if (fieldValue instanceof Serializable) {
            if (fieldValue instanceof Number) {
                json.append("\"").append(fieldName).append("\":").append(fieldValue).append(",");
            } else if (fieldValue instanceof Boolean) {
                json.append("\"").append(fieldName).append("\":").append(fieldValue).append(",");
            } else {
                json.append("\"").append(fieldName).append("\":\"").append(fieldValue).append("\",");
            }
        } else {
            if (findOutGetter(fieldValue)) {
                json.append("\"").append(fieldName).append("\":");
                convert(fieldValue);
                json.append(",");
            } else {
                json.append("\"").append(fieldName).append("\":\"").append(convert(fieldValue)).append("\",");
            }
        }
    }

    private void buildList(List<?> list) {
        json.append("[");
        for (Object object : list) {
            convert(object);
            json.append(",");
        }
        json.setLength(json.length() - 1);
        json.append("]");
    }

    private boolean findOutGetter(Object object) {
        try {
            Field[] fieldArray = object.getClass().getDeclaredFields();
            if (fieldArray.length > 0) {
                String fieldName = fieldArray[0].getName();
                String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                object.getClass().getDeclaredMethod(methodName);
                return true;
            }
        } catch (NoSuchMethodException ignored) {
        }
        return false;
    }

    private boolean isJson(String json) {
        if (json == null || json.isEmpty()) {
            return false;
        }
        if (!json.contains("{")) {
            return false;
        }
        if (json.trim().startsWith("{") && json.trim().endsWith("}")) {
            return checkJson(json, '{', '}') && checkJson(json, '[', ']');
        }
        return false;
    }

    private boolean checkJson(String json, char symbol1, char symbol2) {
        int open = 0;
        int close = 0;
        for (int i = 0; i < json.length(); i++) {
            if (json.charAt(i) == symbol1) {
                ++open;
            }
            if (json.charAt(i) == symbol2) {
                ++close;
            }
        }
        return open == close;
    }

    private <T> T convert(Map<String, Object> map, Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            T object = constructor.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                String fieldName = field.getName();
                String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                setFieldValue(clazz, field, setterName, map, fieldName, object);
            }
            return object;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException ignored) {
        }
        return null;
    }

    private <T> void setFieldValue(Class<T> clazz,
                                   Field field,
                                   String setterName,
                                   Map<String, Object> map,
                                   String fieldName,
                                   T object) {
        try {
            Method[] methodArray = clazz.getMethods();
            for (Method method : methodArray) {
                if (method.getName().equals(setterName)) {
                    Object fieldValue = map.get(fieldName);
                    if (fieldValue != null) {
                        if (field.getType() == UUID.class && fieldValue instanceof String) {
                            method.invoke(object, UUID.fromString((String) fieldValue));
                        } else if (field.getType() == Double.class) {
                            method.invoke(object, Double.parseDouble((String) fieldValue));
                        } else if (field.getType() == String.class) {
                            if (fieldValue instanceof String name) {
                                method.invoke(object, name);
                            }
                        } else if (field.getType() == LocalDate.class) {
                            method.invoke(object, LocalDate.parse((String) fieldValue));
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    private List<?> buildList(String json, Class<?> cls) {
        List<Object> list = new ArrayList<>();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
            String[] elements = json.split(",");
            for (String element : elements) {
                if (cls.isPrimitive() || cls.equals(String.class)) {
                    list.add(element);
                } else {
                    list.add(convert(element, cls));
                }
            }
        }
        return list;
    }
}