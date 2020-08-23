package com.example.socialcode;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @POST("register")
    Call<RegisterAPIResponse> registerUserPost(@Body RegisterAPIBody registerAPIBody);

    @GET("contests")
    Call<ContestAPIResponse> constestsGet();

}
