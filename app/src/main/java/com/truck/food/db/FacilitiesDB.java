package com.truck.food.db;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Dmitry on 25.05.2018.
 */

public class FacilitiesDB extends SugarRecord {

    private String object;

    public FacilitiesDB() {
    }

    public FacilitiesDB(String object) {
        this.object = object;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
