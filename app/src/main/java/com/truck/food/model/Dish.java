package com.truck.food.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 10.05.2018.
 */

public class Dish {

    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("dish_id")
    @Expose
    private int dishId;
    @SerializedName("count")
    @Expose
    private int count;


    public Dish() {
    }

    public Dish(int orderId, int dishId, int count) {
        this.orderId = orderId;
        this.dishId = dishId;
        this.count = count;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int userId) {
        this.orderId = userId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
