package com.example.policetracking.interfaces;

import com.example.policetracking.viewmodels.LatLongRequest;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestAPI {

    String HEADER_POSTFIX = ": ";
    String HEADER_TAG = "@";
    String HEADER_TAG_PUBLIC = HEADER_POSTFIX + "public";

    String HEADER_DOMAIN = "X-Domain-Info";
    String HEADER_DOMAIN_LDWW = HEADER_DOMAIN + ": ldww";

    @POST("/api/v1/auth/token/obtain/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/v1/auth/token/obtain/")
    Call<LoginResponse> sendLatLong(@Body LatLongRequest latLongRequest);

}
