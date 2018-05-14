package com.truck.food.model.dishes_for_av;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class DishForAv
{
    @SerializedName("Menu_ID")
    @Expose
    private String menuId;
    @SerializedName("Quantity")
    @Expose
    private String quantity;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
