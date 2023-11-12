package org.example.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Order {

    private UUID id;
    private List<Product> products;
    private OffsetDateTime createDate;
}
