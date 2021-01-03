package com.example.socialcode;

import android.view.View;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRunnable implements Runnable {

    private View view;
    JSONObject data;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    public MyRunnable(View view){
        this.view =  view;
    }

    @Override
    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<JSONObject> call = jsonPlaceHolderApi.get_problems("vasu2907");
        Response<JSONObject> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = response.body();
    }

    public JSONObject get_data(){
        return this.data;
    }


}
