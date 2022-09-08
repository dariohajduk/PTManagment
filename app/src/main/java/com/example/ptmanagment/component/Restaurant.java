package com.example.ptmanagment.component;

public class Restaurant {

    private String restAddress;
    private String restPhone;
    private String restEmail;
    private String restName;

    public Restaurant(String restAddress, String restPhone, String restEmail) {
        this.restAddress = restAddress;
        this.restPhone = restPhone;
        this.restEmail = restEmail;
    }

    public Restaurant() {
    }

    //region Getter And Setters
    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getRestAddress() {
        return restAddress;
    }

    public void setRestAddress(String restAddress) {
        this.restAddress = restAddress;
    }

    public String getRestPhone() {
        return restPhone;
    }

    public void setRestPhone(String restPhone) {
        this.restPhone = restPhone;
    }

    public String getRestEmail() {
        return restEmail;
    }

    public void setRestEmail(String restEmail) {
        this.restEmail = restEmail;
    }

    //endregion
}
