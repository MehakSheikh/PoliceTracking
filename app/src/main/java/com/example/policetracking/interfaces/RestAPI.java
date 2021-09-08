package com.example.policetracking.interfaces;

import com.example.policetracking.viewmodels.BranchesResponseModel;
import com.example.policetracking.viewmodels.LatLongRequest;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;
import com.example.policetracking.viewmodels.RanksResponseModel;
import com.example.policetracking.viewmodels.RegisterUser;
import com.example.policetracking.viewmodels.UserListing.UserListingModel;
import com.example.policetracking.viewmodels.UsersListingModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestAPI {

    String HEADER_POSTFIX = ": ";
    String HEADER_TAG = "@";
    String HEADER_TAG_PUBLIC = HEADER_POSTFIX + "public";

    String HEADER_DOMAIN = "X-Domain-Info";

    // String HEADER_DOMAIN_LDWW = HEADER_DOMAIN + ": ldww";
    @Headers(HEADER_TAG + HEADER_TAG_PUBLIC)
    @POST("auth/register")
    Call<RegisterUser> registerUser(@Body RegisterUser registerUser);

    @Headers(HEADER_TAG + HEADER_TAG_PUBLIC)
    @GET("rank")
    Call<RanksResponseModel> getRanks();

    @Headers(HEADER_TAG + HEADER_TAG_PUBLIC)
    @GET("branch")
    Call<RanksResponseModel> getBranches();

    @Headers(HEADER_TAG + HEADER_TAG_PUBLIC)
    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @Headers(HEADER_TAG + HEADER_TAG_PUBLIC)
    @POST("location/send")
    Call<LoginResponse> sendLatLong(@Body LatLongRequest latLongRequest);

    @Headers(HEADER_TAG + HEADER_TAG_PUBLIC)
    @GET("user?page=0")
    Call<UserListingModel> getUsers();

}
