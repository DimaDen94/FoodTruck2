package com.solodroid.ecommerce.model.menu_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 15.04.2018.
 */

public class MenuDetail {
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Serve_for")
    @Expose
    private String serveFor;
    @SerializedName("Quantity")
    @Expose
    private String quantity;
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

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String Description)
    {
        this.description = Description;
    }

    public String getServeFor()
    {
        return serveFor;
    }

    public void setServeFor(String Serve_for)
    {
        this.serveFor = Serve_for;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String Quantity)
    {
        this.quantity = Quantity;
    }

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

    @Override
    public String toString()
    {
        return "ClassPojo [Description = "+ description +", Serve_for = "+ serveFor +", Quantity = "+ quantity +", Menu_image = "+ menuImage +", Menu_ID = "+ menuId +", Price = "+ price +", Menu_name = "+ menuName +"]";
    }
}
