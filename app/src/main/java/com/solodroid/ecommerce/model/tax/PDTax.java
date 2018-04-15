package com.solodroid.ecommerce.model.tax;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class PDTax {
    @SerializedName("data")
    @Expose
    private ArrayList<Data> data;

    public ArrayList<Data> getData ()
    {
        return data;
    }

    public void setData (ArrayList<Data> data)
    {
        this.data = data;
    }

}
