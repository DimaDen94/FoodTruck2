package com.truck.food.db;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Dmitry on 11.05.2018.
 */


public class DishDB extends SugarRecord {
    private int count;
    private String description;
    @Unique
    private String menuId;
    private String menuImage;
    private String menuName;
    private String price;
    private String quantity;
    private String serveFor;

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DishDB() {
    }

    public DishDB(String description, String serveFor, String quantity, String menuImage, String menuId, String price, String menuName, int count) {
        this.description = description;
        this.serveFor = serveFor;
        this.quantity = quantity;
        this.menuImage = menuImage;
        this.menuId = menuId;
        this.price = price;
        this.menuName = menuName;
        this.count = count;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }

    public String getServeFor() {
        return this.serveFor;
    }

    public void setServeFor(String Serve_for) {
        this.serveFor = Serve_for;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String Quantity) {
        this.quantity = Quantity;
    }

    public String getMenuImage() {
        return this.menuImage;
    }

    public void setMenuImage(String Menu_image) {
        this.menuImage = Menu_image;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public void setMenuId(String Menu_ID) {
        this.menuId = Menu_ID;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String Price) {
        this.price = Price;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String Menu_name) {
        this.menuName = Menu_name;
    }
}
