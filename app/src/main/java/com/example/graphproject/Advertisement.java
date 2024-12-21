package com.example.graphproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Advertisement implements Serializable
{
    ArrayList<String> images = new ArrayList<>();
    String adTitle;
    String adDescription;
    Integer adPrice;
    Integer adAreaUnit;
    String adPhoneNumber;
    String longitude;
    String latitude;
    String bedrooms;
    String bathrooms;
    String floors;

    public Advertisement(ArrayList<String> images, String adTitle, String adDescription, Integer adPrice, Integer adAreaUnit, String adPhoneNumber, String longitude , String latitude,
                         String bedrooms , String bathrooms , String floors) {
        this.images = images;
        this.adTitle = adTitle;
        this.adDescription = adDescription;
        this.adPrice = adPrice;
        this.adAreaUnit = adAreaUnit;
        this.adPhoneNumber = adPhoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.floors = floors;
    }

    public Advertisement(ArrayList<String> images, String adTitle, Integer adPrice) {
        this.images = images;
        this.adTitle = adTitle;
        this.adPrice = adPrice;
    }

    public String getImages(int index) {
        return images.get(index);
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public Integer getAdPrice() {
        return adPrice;
    }

    public void setAdPrice(Integer adPrice) {
        this.adPrice = adPrice;
    }

    public Integer getAdAreaUnit() {
        return adAreaUnit;
    }

    public void setAdAreaUnit(Integer adAreaUnit) {
        this.adAreaUnit = adAreaUnit;
    }

    public String getAdPhoneNumber() {
        return adPhoneNumber;
    }

    public void setAdPhoneNumber(String adPhoneNumber) {
        this.adPhoneNumber = adPhoneNumber;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getFloors() {
        return floors;
    }

    public void setFloors(String floors) {
        this.floors = floors;
    }

    public ArrayList<String> getImagesList()
    {
        return images;
    }

    public int getImagesListSize()
    {
        return  images.size();
    }
}
