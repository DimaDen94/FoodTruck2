package com.solodroid.ecommerce.retrofit;

import com.solodroid.ecommerce.Constant;
import com.solodroid.ecommerce.model.category.PDCategory;
import com.solodroid.ecommerce.model.menu.PDMenu;
import com.solodroid.ecommerce.model.menu_detail.PDMenuDatail;
import com.solodroid.ecommerce.model.tax.PDTax;

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
}
