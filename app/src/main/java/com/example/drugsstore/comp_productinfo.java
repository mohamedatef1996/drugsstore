package com.example.drugsstore;

public class comp_productinfo {
    String compName,name,description,picurl,price,id;
    public comp_productinfo(){

    }


    public comp_productinfo(String compName, String name, String description, String picurl, String price, String id) {
        this.compName = compName;
        this.name = name;
        this.description = description;
        this.picurl = picurl;
        this.price = price;
        this.id = id;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
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

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
