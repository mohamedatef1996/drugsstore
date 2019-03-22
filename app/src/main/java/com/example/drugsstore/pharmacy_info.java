package com.example.drugsstore;

public class pharmacy_info {
    private  String Name;
    private String Phone;
    private String Loction;
    private String ProfilePicUrl;
    private String secondnumber;
    private String Email;
    private String id;
    private String description;
    public pharmacy_info() {
    }

    public pharmacy_info(String name, String phone, String loction, String profilePicUrl, String secondnumber, String email, String id, String description) {
        Name = name;
        Phone = phone;
        Loction = loction;
        ProfilePicUrl = profilePicUrl;
        this.secondnumber = secondnumber;
        Email = email;
        this.id = id;
        this.description = description;
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

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        ProfilePicUrl = profilePicUrl;
    }

    public String getSecondnumber() {
        return secondnumber;
    }

    public void setSecondnumber(String secondnumber) {
        this.secondnumber = secondnumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
