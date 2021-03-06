package com.truck.food.model.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class Data {
    @SerializedName("Menu")
    @Expose
    private Dish dish;

    public Dish getDish()
    {
        return dish;
    }

    public void setDish(Dish Dish)
    {
        this.dish = Dish;
    }
}
