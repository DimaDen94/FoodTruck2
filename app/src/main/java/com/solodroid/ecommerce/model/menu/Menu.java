package com.solodroid.ecommerce.model.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class Menu
{
    @SerializedName("Menu_image")
    @Expose
    private String menuImage;
    @SerializedName("Menu_ID")
    @Expose
    private String menuId;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("Menu_name")
    @Expose
    private String menuName;

    public String getMenuImage()
    {
        return menuImage;
    }

    public void setMenuImage(String Menu_image)
    {
        this.menuImage = Menu_image;
    }

    public String getMenuId()
    {
        return menuId;
    }

    public void setMenuId(String Menu_ID)
    {
        this.menuId = Menu_ID;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String Price)
    {
        this.price = Price;
    }

    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String Menu_name)
    {
        this.menuName = Menu_name;
    }

}
