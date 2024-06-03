package com.github.supercoding.web.dto;

import java.util.Objects;

public class Item {
    private String id;
    private String name;
    private String type;
    private Integer price;
    private Spec spec;

    public Item() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getPrice() {
        return price;
    }

    public Spec getSpec() {
        return spec;
    }

    public Item(Integer id , ItemBody itemBody) {
        this.id = String.valueOf(id);
        this.name = itemBody.getName();
        this.type = itemBody.getType();
        this.price = itemBody.getPrice();
        this.spec = itemBody.getSpec();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Item item)) return false;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
