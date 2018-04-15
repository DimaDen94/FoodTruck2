package com.solodroid.ecommerce.model.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class Data {
    @SerializedName("Menu")
    @Expose
    private Menu menu;

    public Menu getMenu ()
    {
        return menu;
    }

    public void setMenu (Menu Menu)
    {
        this.menu = Menu;
    }
}
