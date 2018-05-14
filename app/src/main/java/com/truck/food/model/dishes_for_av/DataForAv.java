package com.truck.food.model.dishes_for_av;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class DataForAv {
    @SerializedName("Menu")
    @Expose
    private DishForAv dish;

    public DishForAv getDish()
    {
        return dish;
    }

    public void setDish(DishForAv Dish)
    {
        this.dish = Dish;
    }
}
