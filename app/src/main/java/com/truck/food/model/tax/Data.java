package com.truck.food.model.tax;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class Data {
    @SerializedName("tax_n_currency")
    @Expose
    private TaxOnCurrency taxOnCurrency;

    public TaxOnCurrency getTaxOnCurrency()
    {
        return taxOnCurrency;
    }

    public void setTaxOnCurrency(TaxOnCurrency taxOnCurrency)
    {
        this.taxOnCurrency = taxOnCurrency;
    }


}
