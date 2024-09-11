package by.vitikova.parser.parser;

import java.lang.reflect.Field;

public interface ParserType {

    Object getObject(String value, Field declaredField);
}