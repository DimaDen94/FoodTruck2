package com.truck.food.retrofit;

import com.truck.food.Constant;
import com.truck.food.model.category.PDCategory;
import com.truck.food.model.menu.PDMenu;
import com.truck.food.model.menu_detail.PDMenuDatail;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APIService {

    @GET(Constant.CategoryAPI)
    Call<PDCategory> getAllCategoryData(@QueryMap Map<String, String> map);

    @GET(Constant.MenuAPI)
    Call<PDMenu> getMenu(@QueryMap Map<String, String> map);

    @GET(Constant.MenuDetailAPI)
    Call<PDMenuDatail> getMenuDetail(@QueryMap Map<String, String> map);

    @GET(Constant.SendDataAPI)
    Call<Object> sendUser(@QueryMap Map<String, String> map);

    @GET(Constant.SendOrderAPI)
    Call<Object> sendOrder(@QueryMap Map<String, String> map);


}
