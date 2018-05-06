package com.truck.food.retrofit;

import com.truck.food.Constant;
import com.truck.food.model.category.PDCategory;
import com.truck.food.model.menu.PDMenu;
import com.truck.food.model.menu_detail.PDMenuDatail;
import com.truck.food.model.tax.PDTax;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APIService {

    @GET(Constant.CategoryAPI)
    Call<PDCategory> getAllCategoryData(@QueryMap Map<String, String> map);

    @GET(Constant.TaxCurrencyAPI)
    Call<PDTax> getTaxCurrency(@QueryMap Map<String, String> map);

    @GET(Constant.MenuAPI)
    Call<PDMenu> getMenu(@QueryMap Map<String, String> map);

    @GET(Constant.MenuDetailAPI)
    Call<PDMenuDatail> getMenuDetail(@QueryMap Map<String, String> map);

    @GET(Constant.SendDataAPI)
    Call<Object> sendData(@QueryMap Map<String, String> map);


}
