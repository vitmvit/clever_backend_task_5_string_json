package org.example.model;


import lombok.*;
import org.example.annotation.JsonField;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Customer {

    private UUID id;

    @JsonField("name")
    private String firstName;

    @JsonField("name2")
    private String lastName;
    private LocalDate dateBirth;
    @JsonField("name3")
    private List<Order> orders;
}