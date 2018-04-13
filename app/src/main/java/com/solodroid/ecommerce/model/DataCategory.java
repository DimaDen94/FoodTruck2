package com.solodroid.ecommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 13.04.2018.
 */

public class DataCategory {
    @SerializedName("Category")
    @Expose
    private Category Category;

    public Category getCategory ()
    {
        return Category;
    }

    public void setCategory (Category Category)
    {
        this.Category = Category;
    }
}
