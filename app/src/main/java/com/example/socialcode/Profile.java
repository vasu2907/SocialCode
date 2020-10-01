package com.example.socialcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kosalgeek.android.caching.FileCacheManager;
import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private ImageView pic;
    private DrawerLayout dl;
    private CircleImageView profile_pic;
    private ActionBarDrawerToggle abdt;
    private TextView logout,coderating,codefriends,codecontests;
    private String codeforcesrating,codeforcesfriends,codeforcescontests;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dl = (DrawerLayout) findViewById(R.id.profile_dl);
        abdt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorPrimary));
        }
        else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        pic = (ImageView) findViewById(R.id.profile_profilepic);
        coderating = (TextView)findViewById(R.id.profile_codeforces_rating_num);
        codefriends = (TextView) findViewById(R.id.profile_codeforces_friends_num);
        codecontests = (TextView) findViewById(R.id.profile_codeforces_contest_num);
        profile_pic = (CircleImageView) findViewById(R.id.profile_profilepic);
        intent = getIntent();

        SharedPreferences sharedPref = getSharedPreferences("MyData",Context.MODE_PRIVATE);
        codeforcesrating = sharedPref.getString("codeforces_rating", "N/A");
        codeforcesfriends = sharedPref.getString("codeforces_friends", "N/A");
        codeforcescontests = sharedPref.getString("codeforces_contest", "N/A");
        coderating.setText(codeforcesrating);
        codefriends.setText(codeforcesfriends);
        codecontests.setText(codeforcescontests);
        logout = (TextView) findViewById(R.id.navigation_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileCacheManager manager = new FileCacheManager(getApplicationContext());
                manager.deleteAllCaches();
                SharedPreferences sharedPref = getSharedPreferences("MyData",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("email","");
                editor.putString("password","");
                editor.putString("codeforces_rating", "N/A");
                editor.putString("codeforces_friends", "N/A");
                editor.putString("codeforces_contest", "N/A");
                editor.putString("name", "");
                editor.putString("college", "");
                editor.putString("verified", "False");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.navigation_updateprofile)
                {
                    startActivity( new Intent(getApplicationContext(),UpdateProfile.class));
                }
                else if(id == R.id.navigation_upcomingcontests)
                {
                    startActivity( new Intent(getApplicationContext(), UpcomingContests.class));
                }
                else if(id == R.id.navigation_searchfriend)
                {
                    startActivity( new Intent(getApplicationContext(),SearchFriends.class));
                }
                else if(id == R.id.navigation_favourites)
                {
                    Toast.makeText(getApplicationContext(),"Favourites",Toast.LENGTH_LONG).show();
                }
                else if(id == R.id.navigation_myfriends)
                {
                    Toast.makeText(getApplicationContext(),"My Friends",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        coderating.setText(sharedPref.getString("codeforces_rating", "N/A"));
        codecontests.setText(sharedPref.getString("codeforces_contest", "N/A"));
        codefriends.setText(sharedPref.getString("codeforces_friends", "N/A"));
        FileCacher<HashMap<String, String>> cacher = new FileCacher<>(getApplicationContext(), "user.txt");
        HashMap<String, String> obj = null;
        try {
            obj = cacher.readCache();
            if(!obj.get("image").equals("")){
                byte[] decodeString = Base64.decode(obj.get("image"), Base64.DEFAULT);
                Bitmap bitmap_img = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                profile_pic.setImageBitmap(bitmap_img);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
