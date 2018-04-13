package com.solodroid.ecommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dmitry on 13.04.2018.
 */

public class PDataCategory {
    @SerializedName("data")
    @Expose
    private ArrayList<DataCategory> data;

    public ArrayList<DataCategory> getData() {
        return this.data;
    }

    public void setData(ArrayList<DataCategory> data) {
        this.data = data;
    }
}
