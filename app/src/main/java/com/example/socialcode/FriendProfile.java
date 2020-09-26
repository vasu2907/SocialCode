package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendProfile extends AppCompatActivity {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private TextView Rating, Friends, Contests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        Toast.makeText(getApplicationContext(), "email"+email, Toast.LENGTH_SHORT).show();
//        getSupportActionBar().setTitle(name);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorPrimary));
        }
        else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        Rating = (TextView) findViewById(R.id.profile_codeforces_rating_num);
        Friends = (TextView) findViewById(R.id.profile_codeforces_friends_num);
        Contests = (TextView) findViewById(R.id.profile_codeforces_contest_num);
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String src_email = sharedPref.getString("email", "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        FriendProfileAPIBody body = new FriendProfileAPIBody(src_email, email);
        Call<FriendProfileAPIResponse> call = jsonPlaceHolderApi.get_friend_info(body);
        call.enqueue(new Callback<FriendProfileAPIResponse>() {
            @Override
            public void onResponse(Call<FriendProfileAPIResponse> call, Response<FriendProfileAPIResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                FriendProfileAPIResponse postResponse = response.body();
                String name = postResponse.getName();
                getSupportActionBar().setTitle(name);
                Rating.setText(postResponse.getCodeforces_rating());
                Friends.setText(postResponse.getCodeforces_friends());
                Contests.setText(postResponse.getCodeforces_contest());
            }

            @Override
            public void onFailure(Call<FriendProfileAPIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
