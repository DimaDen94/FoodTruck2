package com.truck.food.model.dishes_for_av;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class PDMenuForAv {
    @SerializedName("data")
    @Expose
    private ArrayList<DataForAv> data;

    public ArrayList<DataForAv> getData ()
    {
        return data;
    }

    public void setData (ArrayList<DataForAv> data)
    {
        this.data = data;
    }
}
