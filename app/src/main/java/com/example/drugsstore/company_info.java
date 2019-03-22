package com.example.drugsstore;

public class company_info {
    private  String Name;
    private String Phone;
    private String Loction;
    private String ProfilePicUrl;
    private String secondnumber;
    private String Email;
    private String id;

    public company_info(String name, String phone, String loction, String profilePicUrl, String secondnumber, String email, String descrbtion,String id) {
        Name = name;
        Phone = phone;
        Loction = loction;
        ProfilePicUrl = profilePicUrl;
        this.secondnumber = secondnumber;
        Email = email;
        this.descrbtion = descrbtion;
        this.id=id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSecondnumber() {
        return secondnumber;
    }

    public void setSecondnumber(String secondnumber) {
        this.secondnumber = secondnumber;
    }

    public String getDescrbtion() {
        return descrbtion;
    }

    public void setDescrbtion(String descrbtion) {
        this.descrbtion = descrbtion;
    }

    private String descrbtion;
    public company_info(){


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        ProfilePicUrl = profilePicUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLoction() {
        return Loction;
    }

    public void setLoction(String loction) {
        Loction = loction;
    }
}
