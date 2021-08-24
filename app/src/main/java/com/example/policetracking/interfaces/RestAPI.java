package com.example.policetracking.interfaces;

import com.example.policetracking.viewmodels.LatLongRequest;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;
import com.example.policetracking.viewmodels.RegisterUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestAPI {

    String HEADER_POSTFIX = ": ";
    String HEADER_TAG = "@";
    String HEADER_TAG_PUBLIC = HEADER_POSTFIX + "public";

    String HEADER_DOMAIN = "X-Domain-Info";
    String HEADER_DOMAIN_LDWW = HEADER_DOMAIN + ": ldww";

    @POST("https://tomcat-server88.paybot.pk/SecurityApp-0.0.1-SNAPSHOT/api/auth/register")
    Call<RegisterUser> registerUser(@Body RegisterUser registerUser);

    @POST("https://tomcat-server88.paybot.pk/SecurityApp-0.0.1-SNAPSHOT/api/auth/register")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/v1/auth/token/obtain/")
    Call<LoginResponse> sendLatLong(@Body LatLongRequest latLongRequest);

}
