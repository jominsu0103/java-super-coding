package com.github.supercoding.web.dto;

import com.github.supercoding.repository.items.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
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
