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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity {

//    private FirebaseAuth auth;
//    private DatabaseReference myref;
    private String codeforces;
//    private FirebaseDatabase database;
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
//        database = FirebaseDatabase.getInstance();
//        myref = database.getReference("Users");
//        auth = FirebaseAuth.getInstance();
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
                        try{
                            HashMap<String, String> obj = new HashMap<String, String>();
                            obj.put("image", postResponse.getBase64());
                            cacher.writeCache(obj);
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
//            auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        myref.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                UserInfo userInfo = dataSnapshot.child(auth.getCurrentUser().getUid())
//                                        .child("Info").getValue(UserInfo.class);
////                                Toast.makeText(getApplicationContext(),"Database",Toast.LENGTH_SHORT).show();
//                                codeforces = userInfo.getCodeforces();
//                                Log.d("Database", codeforces);
//                                fetchdata process = new fetchdata();
//                                process.execute();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
////                        Log.d("DatabaseOut","$$"+codeforces);
//
//                    } else {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent(getApplicationContext(), Login.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }, splashtime);
//                    }
//                }
//            });

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
//    private class fetchdata extends AsyncTask<Void,Void,String> {
//
//        public fetchdata()
//        {
//            super();
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Intent intent = new Intent(getApplicationContext(),Profile.class);
////            intent.putExtra("rating",rating);
////            intent.putExtra("friends",friends);
////            intent.putExtra("contests",contests);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            try{
//
//
//                //// THIS IS FOR CODEFORCES RATING AND FRIENDS
//                Log.d("InsideBackgroundId","$$$"+this.codeforces);
//                URL url = new URL("http://codeforces.com/api/user.info?handles="+this.codeforces+";");
//                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
//                InputStream inputStream =httpURLConnection.getInputStream();
//                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
//                String line ="";
//                result = "";
//                while (line!= null)
//                {
//                    line = bufferedReader.readLine();
//                    result = result + line;
//                }
//                Log.d("Hey","$$$$"+result+"$$$");
//                JSONObject jsonObject = new JSONObject(result);
//                if(jsonObject.getString("status").equals("OK"))
//                {
////                    Toast.makeText(getApplicationContext(),"Valid",Toast.LENGTH_LONG).show();
//                    Log.d("Valid","Jsonobject");
//                    JSONArray temp = jsonObject.getJSONArray("result");
//                    JSONObject c = temp.getJSONObject(0);
//                     this.rating = c.getString("rating");
//                     Log.d("Rating","$$$$"+this.rating);
//                     friends = c.getString("friendOfCount");
//                     Log.d("Friends","$$$"+friends);
//                }
//                else{
//                    Log.d("Null hia kya","####");
//                    return null;
//                }
//                ////THIS IS FOR CODEFORCES CONTESTS
//                result = "";
//                String spec;
//                URL url1 = new URL("https://codeforces.com/api/user.rating?handle="+this.codeforces);
//                HttpURLConnection httpURLConnection1 = (HttpsURLConnection) url1.openConnection();
//                InputStream inputStream1 = httpURLConnection1.getInputStream();
//                BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
//                line = "";
//                while (line!=null)
//                {
//                    line = bufferedReader1.readLine();
//                    result = result + line;
//                }
//
//                JSONObject jsonObject1 = new JSONObject(result);
//                if(jsonObject1.getString("status").equals("OK"))
//                {
//                    JSONArray temp = jsonObject1.getJSONArray("result");
//                    int l = temp.length();
//                    contests = String.valueOf(l);
//                }
//                else
//                {
//                    return null;
//                }
//
//            }catch(MalformedURLException e) {
//                Log.d("MALFORMED",e.getMessage());
//            }catch (IOException e){
//                Log.d("IOEXCEPTION",e.getMessage());
//            } catch (JSONException e) {
//                Log.d("VGVtgc","jbtr");
//                e.printStackTrace();
//            }
//            return null;

//            try {
//                URL url = new URL("http://codeforces.com/api/user.info?handles="+codeforces);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                try {
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                    StringBuilder stringBuilder = new StringBuilder();
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        stringBuilder.append(line).append("\n");
//                    }
//                    bufferedReader.close();
//                    return stringBuilder.toString();
//                } finally {
//                    urlConnection.disconnect();
//                }
//            } catch (Exception e) {
//                Log.e("ERROR", e.getMessage(), e);
//                return null;
//            }
//        }
//    }
