package com.solodroid.ecommerce.model.menu_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class Data {
    @SerializedName("Menu_detail")
    @Expose
    private MenuDetail menuDetail;

    public MenuDetail getMenuDetail()
    {
        return menuDetail;
    }

    public void setMenuDetail(MenuDetail Menu)
    {
        this.menuDetail = Menu;
    }
}
