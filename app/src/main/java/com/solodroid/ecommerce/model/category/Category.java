package com.solodroid.ecommerce.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 13.04.2018.
 */

public class Category {
    @SerializedName("Category_ID")
    @Expose
    private int categoryId;
    @SerializedName("Category_name")
    @Expose
    private String categoryName;
    @SerializedName("Category_image")
    @Expose
    private String categoryImage;


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

}
