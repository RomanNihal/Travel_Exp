package com.example.travelexperience;

public class modelOfData  {
    private String key;
    private String name;
    private String duration;
    private String cost;
    private String image;
    private String description;

    public modelOfData() {
    }

    public modelOfData(String key, String name, String duration, String cost, String image, String description) {
        this.key = key;
        this.name = name;
        this.duration = duration;
        this.cost = cost;
        this.image = image;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}