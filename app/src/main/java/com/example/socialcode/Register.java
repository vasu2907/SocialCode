package com.example.socialcode;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class Register extends AppCompatActivity {
    private EditText Name,College,Email,Password,ReenterPassword,Codechef,Codeforces,Hackerrank;
    private Button Register;
    private TextView signin;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myref;
    private JSONObject obj, resp;
    private String response;
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
        auth =FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();
        obj = new JSONObject();

        myref =database.getReference("Users");


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
        try {
            obj.put("name", name);
            obj.put("college", college);
            obj.put("email", email);
            obj.put("password", pass);
            obj.put("codeforces", codeforces);
            obj.put("codechef", codechef);
            obj.put("hackerrank", hackerrank);
        } catch (JSONException e){
            e.printStackTrace();
        }
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
        Toast.makeText(getApplicationContext(),"Hey There",Toast.LENGTH_SHORT).show();

//        final UserInfo userInfo = new UserInfo(name,college,email,codeforces,codechef,hackerrank);
//        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful())
//                {
//                    Toast.makeText(getApplicationContext(),"Inside",Toast.LENGTH_SHORT).show();
//                    String Uid = auth.getCurrentUser().getUid();
//                    myref.child(Uid).child("Info").setValue(userInfo);
//                    myref.child(Uid).child("Friends").setValue(null);
//                    myref.child(Uid).child("Favourites").setValue(null);
//                    myref.child(Uid).child("Codeforces").setValue(codeforces);
//                    myref.child(Uid).child("Codechef").setValue(codechef);
//                    myref.child(Uid).child("Hackerrank").setValue(hackerrank);
//                    Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getApplicationContext(),Login.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                }
//                else
//                {
//                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
//                    {
//                        Toast.makeText(getApplicationContext(),"Email is already Registered",Toast.LENGTH_LONG).show();
//                    }
//                    else if(task.getException() instanceof FirebaseAuthWeakPasswordException)
//                    {
//                        Toast.makeText(getApplicationContext(),"Password is too weak",Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });

    }

    class fetchdata extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL (" https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/register");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                String data_string = obj.toString();
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = data_string.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                    response = new JSONObject(response.toString());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
