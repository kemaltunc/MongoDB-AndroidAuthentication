package com.example.kemal.mongodb.Retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> createUser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginUser(@FieldMap Map<String, String> params);


}
