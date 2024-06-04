package com.github.supercoding.repository.passenger;

public class Passenger {
    private Integer passengerId;
    private Integer userId;
    private String passportNum;

    public Passenger(Integer passengerId, Integer userId, String passportNum) {
        this.passengerId = passengerId;
        this.userId = userId;
        this.passportNum = passportNum;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getPassportNum() {
        return passportNum;
    }
}
