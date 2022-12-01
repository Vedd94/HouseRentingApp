package com.example.houserenting;

import com.google.firebase.Timestamp;

public class Images {
    private String name;
    private String about;
    private String description;
    private String bhk;
    private String address;
    private String ImageName;
    private String ImageUrl;
    private String userId;
    private Timestamp TimeAdded;
    private String distance;
    private String rent;
    private String Mobile_no;

    public String getMobile_no() {
        return Mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        Mobile_no = mobile_no;
    }



    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }





//    private String phoneno;




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public Images(){

    }
    private static Images instance;
    public static Images getInstance(){
        if(instance == null)
            instance = new Images();
        return instance;
    }

    public Timestamp getTimeAdded() {
        return TimeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        TimeAdded = timeAdded;
    }



    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBhk() {
        return bhk;
    }

    public void setBhk(String bhk) {
        this.bhk = bhk;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }
}
