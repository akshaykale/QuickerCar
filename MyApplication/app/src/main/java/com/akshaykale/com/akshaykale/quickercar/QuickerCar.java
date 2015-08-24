package com.akshaykale.com.akshaykale.quickercar;

import java.util.ArrayList;

/**
 * Created by Akshay on 8/22/2015.
 */

// name, image, price, brand, type, rating, color, engine_cc, mileage, abs_exist, description, link, cities demographic-> city, users
public class QuickerCar {

    private String name;//
    private String image;//
    private float price;//
    private String brand;//
    private String type;//
    private float rating;//
    private String color;//
    private String engine_cc;//
    private String mileage;//
    private boolean abs_exist;//
    private String description;//
    private String link;



    //cities demographic-> city, users'
    private ArrayList<QuickerCity> cities;
    
    public QuickerCar(){
        this. name = "";
        this. image = "";
        float price = 0;
        this. brand = "";
        this. type = "";
        float rating = 0;
        this. color = "";
        float engine_cc = 0;
        float mileage = 0;
        boolean abs_exist = false;
        this. description = "";
        this. link = "";
        //cities demographic-> city, users'
        ArrayList<QuickerCity> cities  = new ArrayList<QuickerCity>();

    }
    
    public QuickerCar(String name, String image, float price, String brand, String type, float rating, String color, String engine_cc, String mileage, boolean abs_exist, String description, String link, ArrayList<QuickerCity> cities) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.brand = brand;
        this.type = type;
        this.rating = rating;
        this.color = color;
        this.engine_cc = engine_cc;
        this.mileage = mileage;
        this.abs_exist = abs_exist;
        this.description = description;
        this.link = link;
        this.cities = cities;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEngine_cc() {
        return engine_cc;
    }

    public void setEngine_cc(String engine_cc) {
        this.engine_cc = engine_cc;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public boolean isAbs_exist() {
        return abs_exist;
    }

    public void setAbs_exist(boolean abs_exist) {
        this.abs_exist = abs_exist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public ArrayList<QuickerCity> getCities() {
        return cities;
    }

    public void setCities(ArrayList<QuickerCity> cities) {
        this.cities = cities;
    }
}
