package com.solodroid.ecommerce;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solodroid.ecommerce.retrofit.APIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dmitry on 13.04.2018.
 */

public class App extends Application {
    private static APIService apiService;
    private Retrofit retrofit;
    private Gson gson = new GsonBuilder().create();



    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.foodtruck8.info") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create(gson)) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        apiService = retrofit.create(APIService.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static APIService getApi() {
        return apiService;
    }
}
