package com.truck.food.db;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Dmitry on 11.05.2018.
 */


public class UserDB extends SugarRecord {
    private String email;


    @Unique
    private String fName;
    private String facility;
    private String lName;
    private boolean last;
    private String phoneNumber;

    public UserDB() {
    }
    public UserDB(String fName, String lName, String phoneNumber, String facility, String email, boolean last) {
        this.fName = fName;
        this.lName = lName;
        this.phoneNumber = phoneNumber;
        this.facility = facility;
        this.email = email;
        this.last = last;
    }

    public boolean isLast() {
        return this.last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public String getfName() {
        return this.fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return this.lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFacility() {
        return this.facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
