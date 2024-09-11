package by.vitikova.parser.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Product {

    private UUID id;
    private String name;
    private Double price;
}