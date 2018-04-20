package com.truck.food.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 15.04.2018.
 */

public class Order {
    @SerializedName("facility")
    @Expose
    private String facility;
    @SerializedName("fname")
    @Expose
    private String fName;
    @SerializedName("lname")
    @Expose
    private String lName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("order_list")
    @Expose
    private String orderList;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("email")
    @Expose
    private String email;

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
