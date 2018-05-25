package com.truck.food.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 25.05.2018.
 */

public class Facilities {
    @SerializedName("objects")
    @Expose
    private String[] objects;

    public String[] getObjects ()
    {
        return objects;
    }

    public void setObjects (String[] objects)
    {
        this.objects = objects;
    }
}
