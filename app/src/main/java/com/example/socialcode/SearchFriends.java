package com.example.socialcode;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFriends extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<FriendsInfo> arrayList;
    EditText searchbar;
    ImageView search_btn;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    String res = "";
//    private DatabaseReference myref;
    private FriendsSearchAdapter friendsSearchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        getSupportActionBar().setTitle("Friends");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorPrimary));
        }
        else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        recyclerView = (RecyclerView) findViewById(R.id.searchfriends_recycler);
        searchbar =(EditText) findViewById(R.id.searchfriends_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        arrayList = new ArrayList<>();
//        myref = FirebaseDatabase.getInstance().getReference("Users");
        search_btn = (ImageView) findViewById(R.id.search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = searchbar.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "keyowrd:"+keyword, Toast.LENGTH_LONG).show();
                if(keyword.equals("") == true){
                    Toast.makeText(getApplicationContext(), "No Users Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                setAdapter(keyword);
            }
        });

//        searchbar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable st) {
//                if(!st.toString().isEmpty())
//                {
//                    setAdapter(st.toString());
//                }
//                else
//                {
//                    arrayList.clear();
//                }
//            }
//        });

    }

    private void setAdapter(String pattern){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        SearchFriendAPIBody body = new SearchFriendAPIBody(pattern);
        Call<SearchFriendAPIResponse> call = jsonPlaceHolderApi.search_friends(body);
        call.enqueue(new Callback<SearchFriendAPIResponse>() {
            @Override
            public void onResponse(Call<SearchFriendAPIResponse> call, Response<SearchFriendAPIResponse> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "No Users Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                SearchFriendAPIResponse postResponse = response.body();
                ArrayList<HashMap<String, String>> payload = postResponse.getPayload();

                for(HashMap<String, String> obj: payload){
                    arrayList.add(new FriendsInfo(obj.get("name"),obj.get("email"),"", ""));
                }
                if(arrayList.isEmpty() == true){
                    Toast.makeText(getApplicationContext(), "No Users Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                friendsSearchAdapter = new FriendsSearchAdapter(getApplicationContext(), arrayList);
                recyclerView.setAdapter(friendsSearchAdapter);
            }

            @Override
            public void onFailure(Call<SearchFriendAPIResponse> call, Throwable t) {

            }
        });
    }

//    private void setAdapter(final String searchstring)
//    {
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                arrayList.clear();
//                int counter = 0;
//                for( DataSnapshot snapshot : dataSnapshot.getChildren())
//                {
//                        String uid = snapshot.getKey();
//                        UserInfo userInfo =snapshot.child("Info").getValue(UserInfo.class);
//                        String profilepic = uid;
//                        String name = userInfo.getName();
//                        String email = userInfo.getEmail();
//                        String codeforces_handle = userInfo.getCodeforces();
//                        if(name.toLowerCase().contains(searchstring.toLowerCase()))
//                        {
//                            FriendsInfo friendsInfo = new FriendsInfo(name,email,profilepic,codeforces_handle);
//                            arrayList.add(friendsInfo);
//                            counter+=1;
//                        }
//                        else if(email.toLowerCase().contains(searchstring.toLowerCase()))
//                        {
//                            FriendsInfo friendsInfo = new FriendsInfo(name,email,profilepic,codeforces_handle);
//                            arrayList.add(friendsInfo);
//                            counter+=1;
//                        }
//                        if(counter == 10)
//                            break;
//                }
//
//                friendsSearchAdapter = new FriendsSearchAdapter(getApplicationContext(),arrayList);
//                recyclerView.setAdapter(friendsSearchAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
