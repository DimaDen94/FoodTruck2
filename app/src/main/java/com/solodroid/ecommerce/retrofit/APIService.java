package com.solodroid.ecommerce.retrofit;

import com.solodroid.ecommerce.model.PDataCategory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface APIService {

    @GET("/panel/api/get-all-category-data.php")
    Call<PDataCategory> getAllCategoryData(@QueryMap Map<String, String> map);
}
