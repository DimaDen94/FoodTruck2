package com.truck.food.model.menu_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dmitry on 15.04.2018.
 */

public class PDMenuDatail {
    @SerializedName("data")
    @Expose
    private ArrayList<Data> data;

    public ArrayList<Data> getData ()
    {
        return data;
    }

    public void setData (ArrayList<Data> data)
    {
        this.data = data;
    }
}
