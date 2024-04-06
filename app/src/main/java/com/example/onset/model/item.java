package com.example.onset.model;

public class item {
    private  String location;
    private  String price,name;
    private String imageUrl;
   // private List<String> images;
    private  String description;
    private  String shortDescription;
    private  String totalRooms;
    private String apartmentID,rentType,ownerID;
    private boolean isWifi,isParking,isWater,isSecurity, isLiked;
    private int likesCount;


    /* public item(String ownerID) {


        this.ownerID=ownerID;
    }*/
    public item(String name,String ownerID,String location, String price, String rentType, String imageUrl, String shortDescription, String totalRooms, String apartmentID, boolean isWifi, boolean isParking, boolean isWater, boolean isSecurity) {

        this.location = location;
        this.name=name;
        this.rentType=rentType;
        this.price = price;
        this.imageUrl = imageUrl;
        this.shortDescription = shortDescription;
        this.apartmentID = apartmentID;
        this.isWifi = isWifi;
        this.totalRooms=totalRooms;
        this.isParking = isParking;
        this.isWater = isWater;
        this.isSecurity = isSecurity;
        this.ownerID=ownerID;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWifi() {
        return isWifi;
    }

    public void setWifi(boolean wifi) {
        isWifi = wifi;
    }

    public boolean isParking() {
        return isParking;
    }

    public void setParking(boolean parking) {
        isParking = parking;
    }

    public boolean isWater() {
        return isWater;
    }

    public void setWater(boolean water) {
        isWater = water;
    }

    public boolean isSecurity() {
        return isSecurity;
    }

    public void setSecurity(boolean security) {
        isSecurity = security;
    }

    public String getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(String apartmentID) {
        this.apartmentID = apartmentID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public void setImages(String imageUrl) {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(String totalImages) {
        this.totalRooms = totalImages;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }
}
