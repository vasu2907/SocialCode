package com.example.socialcode;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Register extends AppCompatActivity {
    private EditText Name,College,Email,Password,ReenterPassword,Codechef,Codeforces,Hackerrank;
    private Button Register;
    private TextView signin;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String responseBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Name = (EditText)findViewById(R.id.register_name);
        College = (EditText) findViewById(R.id.register_college);
        Email = (EditText) findViewById(R.id.register_email);
        Password = (EditText) findViewById(R.id.register_pass);
        ReenterPassword = (EditText) findViewById(R.id.register_reenterpass);
        Codechef = (EditText) findViewById(R.id.register_codechef);
        Codeforces = (EditText) findViewById(R.id.register_codeforces);
        Hackerrank = (EditText) findViewById(R.id.register_hackerrank);
        Register = (Button) findViewById(R.id.register_register);
        signin = (TextView) findViewById(R.id.register_signin);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addUser()
    {
        final String name,college,email,pass,repass,codechef,codeforces,hackerrank;
        name = Name.getText().toString().trim();
        college = College.getText().toString().trim();
        email = Email.getText().toString().trim();
        pass = Password.getText().toString().trim();
        repass = ReenterPassword.getText().toString().trim();
        codeforces = Codeforces.getText().toString().trim();
        codechef = Codechef.getText().toString().trim();
        hackerrank = Hackerrank.getText().toString().trim();
        if(name.isEmpty())
        {
            Name.setError("Enter your Name");
            Name.requestFocus();
            return;
        }
        if(college.isEmpty())
        {
            College.setError("Enter College Name");
            College.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            Email.setError("Enter your Email");
            Email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Enter a valid Email Address");
            Email.requestFocus();
            return;
        }
        if(pass.isEmpty())
        {
            Password.setError("Enter your Password");
            Password.requestFocus();
            return;
        }
        if(pass.length()<8)
        {
            Password.setError("Minimum Password Length is 8");
            Password.requestFocus();
            return;
        }
        if(repass.isEmpty())
        {
            ReenterPassword.setError("Enter your Password again");
            ReenterPassword.requestFocus();
            return;
        }
        if(!repass.equals(pass))
        {
            ReenterPassword.setError("Passwords do not match");
            ReenterPassword.requestFocus();
            return;
        }
        if(codeforces.isEmpty())
        {
            Codeforces.setError("Enter your Handle");
            Codeforces.requestFocus();
            return;
        }
        if(codechef.isEmpty())
        {
            Codechef.setError("Enter your Handle");
            Codechef.requestFocus();
            return;
        }
        if(hackerrank.isEmpty())
        {
            Hackerrank.setError("Enter your Handle");
            Hackerrank.requestFocus();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        RegisterAPIBody body = new RegisterAPIBody(name, email, pass, college, codeforces, codechef, hackerrank);
        Call<RegisterAPIResponse> call = jsonPlaceHolderApi.registerUserPost(body);
        call.enqueue(new Callback<RegisterAPIResponse>() {
            @Override
            public void onResponse(Call<RegisterAPIResponse> call, Response<RegisterAPIResponse> response) {
                if(!response.isSuccessful()) {
                    responseBody = "Some Error Occurred";
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                    return ;
                }
                RegisterAPIResponse postResponse = response.body();
                responseBody = postResponse.getMessage();
                Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<RegisterAPIResponse> call, Throwable t) {
                responseBody = "Internal Server Error";
                Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
            }
        });
    }
}
