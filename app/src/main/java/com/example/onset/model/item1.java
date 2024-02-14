package com.example.onset.model;

public class item1 {
    String price;
    String imageUrl;

    public item1(){

    }

    public item1(String price, String imageUrl) {
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getPrice() {

        return price;

    }


    public void setPrice(String price ) {

        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
