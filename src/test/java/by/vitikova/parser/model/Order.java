package by.vitikova.parser.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Order {

    private UUID id;
    private List<Product> products;
    private OffsetDateTime createDate;
}