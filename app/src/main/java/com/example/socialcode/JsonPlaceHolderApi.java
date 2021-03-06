package com.example.socialcode;

import org.json.JSONObject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @POST("register")
    Call<RegisterAPIResponse> registerUserPost(@Body RegisterAPIBody registerAPIBody);

    @POST("login")
    Call<LoginAPIResponse> loginUserPost(@Body LoginAPIBody loginAPIBody);

    @GET("contest")
    Call<ContestAPIResponse> constestsGet();

    @GET("user/{id}")
    Call<DynamoUserInfo> getUserInfo(@Path("id") String email);

    @POST("user/{id}")
    Call<SaveProfileResponse> saveProfileData(@Path("id") String email, @Body SaveProfileBody saveProfileBody);

    @PUT("user/{id}")
    Call<RetrieveDataResponse> retrieveData(@Path("id") String email);

    @POST("verify")
    Call<RegisterAPIResponse> verify_user(@Body VerifyUserBody verifyUserBody);

    @POST("friend")
    Call<SearchFriendAPIResponse> search_friends(@Body SearchFriendAPIBody searchFriendAPIBody);

    @POST("friend/profile")
    Call<FriendProfileAPIResponse> get_friend_info(@Body FriendProfileAPIBody friendProfileAPIBody);

    @PUT("friend")
    Call<RetrieveDataResponse> connect_req_handler(@Body ConnectionReqAPIBody connectionReqAPIBody);

    @POST("user/{id}/problems")
    Call<JSONObject> get_problems(@Path("id") String handle);
}
