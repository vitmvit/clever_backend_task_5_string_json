package by.vitikova.parser.util;

import by.vitikova.parser.annotation.JsonField;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class JsonFieldTestBuilder {

    @JsonField("firstName")
    @Builder.Default
    private String name = "name";

    @JsonField("secondName")
    @Builder.Default
    private String surname = "surname";

    @Builder.Default
    private int age = 20;
}