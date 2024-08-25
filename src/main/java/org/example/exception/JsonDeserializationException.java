package org.example.exception;

import static org.example.constant.Constant.OBJECT_GENERATION_ERROR_MESSAGE;

public class JsonDeserializationException extends RuntimeException {

    public JsonDeserializationException() {
        super(OBJECT_GENERATION_ERROR_MESSAGE);
    }

    public JsonDeserializationException(String message) {
        super(message);
    }
}