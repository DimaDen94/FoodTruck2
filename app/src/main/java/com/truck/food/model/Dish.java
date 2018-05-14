package com.truck.food.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 10.05.2018.
 */

public class Dish {

    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("dish_id")
    @Expose
    private int dishId;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("comment")
    @Expose
    private String comment;

    public Dish() {
    }

    public Dish(int userId, int dishId, int count, String date, String comment) {
        this.userId = userId;
        this.dishId = dishId;
        this.count = count;
        this.date = date;
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
