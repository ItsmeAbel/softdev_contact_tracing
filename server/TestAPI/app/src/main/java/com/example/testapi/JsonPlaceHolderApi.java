package com.example.testapi;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @FormUrlEncoded
    @POST("core/register/")
    Call<User> createUser(
            @Field("email") String email,
            @Field("password") String password
        );
    @FormUrlEncoded
    @POST("core/register/")
    Call<User> createUser(@FieldMap Map<String, String> fields);

}
