package com.example.socialcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private TextView logout,coderating,codefriends,codecontests;
    private String codeforcesrating,codeforcesfriends,codeforcescontests;
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
        coderating = (TextView)findViewById(R.id.profile_codeforces_rating_num);
        codefriends = (TextView) findViewById(R.id.profile_codeforces_friends_num);
        codecontests = (TextView) findViewById(R.id.profile_codeforces_contest_num);
        Intent intent = getIntent();
        codeforcesrating = intent.getStringExtra("rating");
        codeforcesfriends = intent.getStringExtra("friends");
        codeforcescontests = intent.getStringExtra("contests");
        coderating.setText(codeforcesrating);
        Log.d("Rating","$$$$"+codeforcesrating);
        codefriends.setText(codeforcesfriends);
        codecontests.setText(codeforcescontests);
        logout = (TextView) findViewById(R.id.navigation_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("MyData",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Email","");
                editor.putString("Password","");
                editor.commit();
                FirebaseAuth.getInstance().signOut();
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
                    Toast.makeText(getApplicationContext(),"Update Profile",Toast.LENGTH_LONG).show();
                    startActivity( new Intent(getApplicationContext(),UpdateProfile.class));
                }
                else if(id == R.id.navigation_upcomingcontests)
                {
                    Toast.makeText(getApplicationContext(),"Upcoming Contests",Toast.LENGTH_LONG).show();
                    startActivity( new Intent(getApplicationContext(), UpcomingContests.class));
                }
                else if(id == R.id.navigation_searchfriend)
                {
                    Toast.makeText(getApplicationContext(),"Search Friend",Toast.LENGTH_LONG).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {

        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
