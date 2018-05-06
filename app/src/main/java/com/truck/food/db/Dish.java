package com.truck.food.db;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Dmitry on 24.04.2018.
 */

public class Dish extends SugarRecord {

    private String description;

    private String serveFor;

    private String quantity;

    private String menuImage;
    @Unique
    private String menuId;

    private String price;

    private String menuName;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Dish() {
    }

    public Dish(String description, String serveFor, String quantity, String menuImage, String menuId, String price, String menuName, int count) {
        this.description = description;
        this.serveFor = serveFor;
        this.quantity = quantity;
        this.menuImage = menuImage;
        this.menuId = menuId;
        this.price = price;
        this.menuName = menuName;
        this.count = count;
    }

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

}
