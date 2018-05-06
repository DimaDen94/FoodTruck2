package com.truck.food;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarApp;
import com.truck.food.retrofit.APIService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dmitry on 13.04.2018.
 */

public class App extends SugarApp {
    private static APIService apiService;
    private Retrofit retrofit;
    private Gson gson = new GsonBuilder().setLenient()
            .create();



    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BaseURL)
                .client(new OkHttpClient.Builder().build())//Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create(gson)) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        apiService = retrofit.create(APIService.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static APIService getApi() {
        return apiService;
    }
}
