package com.example.onset.model;

import java.util.Collections;
import java.util.List;

public class item {
    private  String location;
    private  String price;
    private String imageUrl;
    private List<String> images;
    private  String description;
    private  String shortDescription;
    private  int totalImages;
   // private String imageUrl;



/*    public item(String location, String price,String shortDescription) {
        this.location = location;
        this.price = price;
        this.shortDescription = shortDescription;
    }*/

    public item( List<String>images,String imageUrl,String price){
        this.location=location;
        this.price=price;
        this.imageUrl=imageUrl;
        this.images=images;
        this.description=description;
        this.shortDescription=shortDescription;
        this.totalImages=totalImages;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(String imageUrl) {
        // Assuming imageUrl is a single image URL, convert it to a List<Integer> if needed
        // For example, if imageUrl is a URL like "https://example.com/image.jpg"
        // and you want to convert it to a list containing a single element, you can do:
      //  this.images = Collections.singletonList(imageUrl.hashCode());
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

    public int getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(int totalImages) {
        this.totalImages = totalImages;
    }
}
