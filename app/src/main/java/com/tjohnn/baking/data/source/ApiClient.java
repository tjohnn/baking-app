package com.tjohnn.baking.data.source;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;


public class ApiClient {

    private static ResourceApiService resourceApiService;
    private static Gson gson;

    public static synchronized ResourceApiService getResourceApiService() {
        if (resourceApiService == null) {
            String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
            Retrofit.Builder retrofitBuilder;
            retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(BASE_URL);
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofitBuilder.client(getOkHttpClient());
            retrofitBuilder.addConverterFactory(gsonConverterFactory());
            resourceApiService = retrofitBuilder.build().create(ResourceApiService.class);
        }
        return resourceApiService;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor());
        return builder.build();
    }


    private static HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.d(message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


    private static Gson gson(){
        if(gson == null){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

            gson = gsonBuilder.create();
        }
        return gson;
    }

    private static GsonConverterFactory gsonConverterFactory(){
        return GsonConverterFactory.create(gson());
    }

}
