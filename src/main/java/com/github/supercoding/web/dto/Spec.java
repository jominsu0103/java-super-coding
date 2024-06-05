package com.github.supercoding.web.dto;

import lombok.ToString;

@ToString
public class Spec {
    private String cpu;
    private String capacity;

    public Spec() {
    }

    public Spec(String cpu, String capacity) {
        this.cpu = cpu;
        this.capacity = capacity;
    }

    public String getCpu() {
        return cpu;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
