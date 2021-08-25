package com.example.policetracking.interfaces;

import com.example.policetracking.viewmodels.BranchesResponseModel;
import com.example.policetracking.viewmodels.LatLongRequest;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;
import com.example.policetracking.viewmodels.RanksResponseModel;
import com.example.policetracking.viewmodels.RegisterUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestAPI {

    String HEADER_POSTFIX = ": ";
    String HEADER_TAG = "@";
    String HEADER_TAG_PUBLIC = HEADER_POSTFIX + "public";

    String HEADER_DOMAIN = "X-Domain-Info";
    String HEADER_DOMAIN_LDWW = HEADER_DOMAIN + ": ldww";

    @POST("auth/register")
    Call<RegisterUser> registerUser(@Body RegisterUser registerUser);

    @GET("api/rank")
    Call<RanksResponseModel> getRanks();

    @GET("branch")
    Call<BranchesResponseModel> getBranches();

    @POST("")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("")
    Call<LoginResponse> sendLatLong(@Body LatLongRequest latLongRequest);

}
