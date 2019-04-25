package com.example.kemal.mongodb.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static final String URL = "http://10.57.33.100:3000/";

    private static Client mInstance;
    private Retrofit retrofit;


    private Client() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized Client getmInstance() {
        if (mInstance == null) {
            mInstance = new Client();
        }
        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
