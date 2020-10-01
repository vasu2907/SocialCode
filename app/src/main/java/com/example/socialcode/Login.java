package com.example.socialcode;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kosalgeek.android.caching.FileCacheManager;
import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    private EditText username,password;
    private Button login;
    private Button signup;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String responseBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(" Login");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorPrimary));
        }
        else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_loginbtn);
        signup = (Button) findViewById(R.id.login_signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action;
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginuser()
    {
        final String email,pass;
        email = username.getText().toString().trim();
        pass = password.getText().toString().trim();

        if(email.isEmpty())
        {
            username.setError("Email is Required");
            username.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            username.setError("Enter a valid Email Address");
            username.requestFocus();
            return;
        }
        if(pass.isEmpty())
        {
            password.setError("Password is Required");
            password.requestFocus();
            return;
        }
        if(pass.length()<8)
        {
            password.setError("Minimum Password Length is 8");
            password.requestFocus();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        LoginAPIBody body = new LoginAPIBody(email, pass);
        Call<LoginAPIResponse> call = jsonPlaceHolderApi.loginUserPost(body);
        call.enqueue(new Callback<LoginAPIResponse>() {
            @Override
            public void onResponse(Call<LoginAPIResponse> call, Response<LoginAPIResponse> response) {
                if(!response.isSuccessful()) {
                    responseBody = "Login Failed";
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                    return;
                }
                LoginAPIResponse postResponse = response.body();
                responseBody = postResponse.getMessage();
                if(postResponse.getCredentials() == true) {
                    FileCacheManager manager = new FileCacheManager(Login.this);
                    manager.deleteAllCaches();
                    FileCacher<HashMap<String, String>> cacher = new FileCacher<HashMap<String, String>>(Login.this, "user.txt");
                    try{
                        HashMap<String, String> obj = new HashMap<String, String>();
                        obj.put("image", postResponse.getBase64());
                        cacher.writeCache(obj);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor =sharedPref.edit();
                    editor.putString("email",email);
                    editor.putString("password",pass);
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
                    responseBody = "Invalid Credentials";
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginAPIResponse> call, Throwable t) {
                responseBody = "Some Error Occurred";
                Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
            }
        });
    }
}
