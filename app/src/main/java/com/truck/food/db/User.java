package com.truck.food.db;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Dmitry on 04.05.2018.
 */

public class User extends SugarRecord {
    @Unique
    private String fName;
    private String lName;
    private String phoneNumber;
    private String facility;
    private String email;
    private boolean last;

    public User(String fName, String lName, String phoneNumber, String facility, String email, boolean last) {
        this.fName = fName;
        this.lName = lName;
        this.phoneNumber = phoneNumber;
        this.facility = facility;
        this.email = email;
        this.last = last;
    }

    public User() {
    }



    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
