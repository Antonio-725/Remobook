package com.example.onset;

public class Apartment {
    private String id;
    private String ownerId;
    private String name;
    private String description;
    private String price;
    private String location;
    private String roomNumber;
    private boolean security;
    private boolean water;
    private boolean parking;
    private boolean wifi;
    private String rentType;
    private String apartmentType;


    // Constructor
    public Apartment( String name,String ownerId, String description, String price, String location, String roomNumber,
                     boolean security, boolean water, boolean parking, boolean wifi, String rentType,String apartmentType) {

        this.name = name;
        this.ownerId=ownerId;

        this.description = description;
        this.price = price;
        this.location = location;
        this.roomNumber = roomNumber;
        this.security = security;
        this.water = water;
        this.parking = parking;
        this.wifi = wifi;
        this.rentType = rentType;
        this.apartmentType=apartmentType;


    }

    // Getters and Setters (You can generate them automatically in most IDEs)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }
}
