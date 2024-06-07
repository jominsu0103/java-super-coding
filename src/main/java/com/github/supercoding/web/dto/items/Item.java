package com.github.supercoding.web.dto.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Item {
    private String id;
    private String name;
    private String type;
    private Integer price;
    private Spec spec;
}
