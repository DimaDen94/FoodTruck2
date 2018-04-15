package com.solodroid.ecommerce.model.tax;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry on 14.04.2018.
 */

public class TaxOnCurrency {
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("Variable")
    @Expose
    private String variable;

    public String getValue ()
    {
        return value;
    }

    public void setValue (String Value)
    {
        this.value = Value;
    }

    public String getVariable ()
    {
        return variable;
    }

    public void setVariable (String Variable)
    {
        this.variable = Variable;
    }
}
