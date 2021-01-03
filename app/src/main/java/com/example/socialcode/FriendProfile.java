package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendProfile extends AppCompatActivity {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private TextView Rating, Friends, Contests;
    private CircleImageView profilepic;
    private ImageView friend_icon;
    private String base64;


    private RecyclerView recyclerViewParent;
    ArrayList<ParentChild> parentChildObj;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final String email = intent.getStringExtra("email");
        getSupportActionBar().setTitle(name);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorPrimary));
        }
        else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        base64 = "";
        Rating = (TextView) findViewById(R.id.profile_codeforces_rating_num);
        Friends = (TextView) findViewById(R.id.profile_codeforces_friends_num);
        Contests = (TextView) findViewById(R.id.profile_codeforces_contest_num);
        profilepic = (CircleImageView) findViewById(R.id.profile_profilepic);
        friend_icon = (ImageView) findViewById(R.id.add_friend);

        friend_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_friend(email);
            }
        });

        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String src_email = sharedPref.getString("email", "");

        recyclerViewParent = (RecyclerView) findViewById(R.id.rv_parent);
        final LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);


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
                parentChildObj = createData(postResponse.getProblems());
                ParentAdapter parentAdapter = new ParentAdapter(getApplicationContext(), parentChildObj);
                recyclerViewParent.setAdapter(parentAdapter);
                recyclerViewParent.setLayoutManager(manager);
                recyclerViewParent.setHasFixedSize(true);
                if(postResponse.getFriend()){
                    friend_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.friend));
                }
                base64 = postResponse.getBase64();
                if(!base64.equals("")){
                    byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profilepic.setImageBitmap(decodedByte);
                }
            }

            @Override
            public void onFailure(Call<FriendProfileAPIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private ArrayList<ParentChild> createData(HashMap<String, ArrayList<String>> object){
        parentChildObj = new ArrayList<>();
        if( object == null){
            return parentChildObj;
        }
        for(String category: object.keySet()){
            ArrayList<String> arrayList = object.get(category);
            ParentChild obj = new ParentChild();
            ArrayList<Child> list = new ArrayList<>();
            Child c = new Child();
            c.setChild_name(toTitleCase(category));
            list.add(c);
            for(int i=0;i<arrayList.size();i++){
                Child ch = new Child();
                ch.setChild_name(arrayList.get(i));
                list.add(ch);
            }
            obj.setChild(list);
            parentChildObj.add(obj);
        }
        return parentChildObj;
    }

    private String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    private Boolean isFriend(@NonNull ImageView view){
        if(view.getTag() == "friend"){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private void add_friend(String dest){
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String src = sharedPref.getString("email", "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        ConnectionReqAPIBody body = new ConnectionReqAPIBody(src, dest);
        Call<RetrieveDataResponse> call = jsonPlaceHolderApi.connect_req_handler(body);
        call.enqueue(new Callback<RetrieveDataResponse>() {
            @Override
            public void onResponse(Call<RetrieveDataResponse> call, Response<RetrieveDataResponse> response) {
                if(!response.isSuccessful()){
                    String msg = "";
                    if(isFriend(friend_icon)){
                        msg = "Failed to Unfriend";
                    } else {
                        msg = "Failed to add Friend";
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isFriend(friend_icon)){
                    friend_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_friend));
                    friend_icon.setTag("add");
                } else {
                    friend_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.friend));
                    friend_icon.setTag("friend");
                }
            }

            @Override
            public void onFailure(Call<RetrieveDataResponse> call, Throwable t) {
                String msg = "";
                if(isFriend(friend_icon)){
                    msg = "Failed to Unfriend";
                } else {
                    msg = "Failed to add Friend";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
