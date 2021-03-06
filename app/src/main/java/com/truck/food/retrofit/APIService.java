package com.truck.food.retrofit;

import com.truck.food.Constant;
import com.truck.food.model.Facilities;
import com.truck.food.model.category.PDCategory;
import com.truck.food.model.dishes_for_av.PDMenuForAv;
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

    @GET(Constant.AllTheDishesAPI)
    Call<PDMenuForAv> getAllTheDishes(@QueryMap Map<String, String> map);

    @GET(Constant.MenuDetailAPI)
    Call<PDMenuDatail> getMenuDetail(@QueryMap Map<String, String> map);

    @GET(Constant.SendUserAPI)
    Call<Object> sendUser(@QueryMap Map<String, String> map);

    @GET(Constant.SendOrderAPI)
    Call<Object> sendOrder(@QueryMap Map<String, String> map);

    @GET(Constant.SendOrderListAPI)
    Call<Object> sendOrderList(@QueryMap Map<String, String> map);

    @GET(Constant.CheckNotification)
    Call<Object> checkNotification();

    @GET(Constant.ObjectsAPI)
    Call<Facilities> getObjects();


}
