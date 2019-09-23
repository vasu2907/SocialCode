package com.example.socialcode;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.EditText;

import java.util.ArrayList;

public class SearchFriends extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<String> arrayList;
    EditText searchbar;
    String res = "";
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
        
    }
}
