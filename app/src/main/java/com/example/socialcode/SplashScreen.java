package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosalgeek.android.caching.FileCacheManager;
import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity {

    private String codeforces;
    static int splashtime = 1500;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String responseBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final String email, password;
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        email = sharedPref.getString("email", "");
        password = sharedPref.getString("password", "");
        if (!email.isEmpty() && !password.isEmpty()) {
            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            LoginAPIBody body = new LoginAPIBody(email, password);
            Call<LoginAPIResponse> call = jsonPlaceHolderApi.loginUserPost(body);
            call.enqueue(new Callback<LoginAPIResponse>() {
                @Override
                public void onResponse(Call<LoginAPIResponse> call, Response<LoginAPIResponse> response) {
                    if(!response.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    LoginAPIResponse postResponse = response.body();
                    responseBody = postResponse.getMessage();
                    if(postResponse.getCredentials() == true) {
                        responseBody = "Login Successful";
                        FileCacheManager manager =  new FileCacheManager(getApplicationContext());
                        manager.deleteAllCaches();
                        FileCacher<HashMap<String, String>> cacher = new FileCacher<HashMap<String, String>>(getApplicationContext(), "user.txt");
                        FileCacher<HashMap<String, ArrayList<String>>> problemsCacher = new FileCacher<HashMap<String, ArrayList<String>>>(getApplicationContext(), "problems.txt");
                        try{
                            HashMap<String, String> obj = new HashMap<String, String>();
                            obj.put("image", postResponse.getBase64());
                            cacher.writeCache(obj);
                            problemsCacher.writeCache(postResponse.getProblems());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor =sharedPref.edit();
                        editor.putString("Email",email);
                        editor.putString("Password",password);
                        editor.putString("codeforces_rating", postResponse.getCodeforces_rating());
                        editor.putString("codeforces_friends", postResponse.getCodeforces_friends());
                        editor.putString("codeforces_contest", postResponse.getCodeforces_contests());
                        editor.putString("name", postResponse.getName());
                        editor.putString("college", postResponse.getCollege());
                        editor.putString("verified", postResponse.getVerified()?"True":"False");
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(),Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        responseBody = "Login Failed";
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    return;
                }

                @Override
                public void onFailure(Call<LoginAPIResponse> call, Throwable t) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }, splashtime);
        }
    }
}
