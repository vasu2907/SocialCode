package com.example.socialcode;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpcomingContests extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<ContestsInfo> arrayList;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String responseBody;
    String rest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_contests);
        getSupportActionBar().setTitle("Contests");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorPrimary));
        }
        else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        recyclerView = (RecyclerView) findViewById(R.id.contest_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        arrayList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<ContestAPIResponse> call = jsonPlaceHolderApi.constestsGet();
        call.enqueue(new Callback<ContestAPIResponse>() {
            @Override
            public void onResponse(Call<ContestAPIResponse> call, Response<ContestAPIResponse> response) {
                if(!response.isSuccessful()) {
                    responseBody = "Internal Server Error";
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                    return;
                }

                ContestAPIResponse getResponse = response.body();
                List<HashMap<String, String>> contests = getResponse.getContest();
                for (HashMap<String, String> temp: contests) {
                    ContestsInfo contestsInfo = new ContestsInfo(
                            temp.get("name"),
                            temp.get("contest_duration"),
                            temp.get("contest_starttime"),
                            temp.get("contest_date"),
                            temp.get("id"));
                    arrayList.add(contestsInfo);
                }
                Collections.reverse(arrayList);
                SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(), arrayList);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onFailure(Call<ContestAPIResponse> call, Throwable t) {
                responseBody = "Failed to Load Contests List";
                Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
            }
        });

    }

    public String gettime(int st)
    {
        String res = "";
        int hours = st/3600;
        st = st%3600;
        int min = st/60;
        int sec = st%60;
        if(hours<10)
        {
            res+="0"+String.valueOf(hours)+":";
        }
        else
        {
            res+=String.valueOf(hours)+":";
        }

        if(min<10)
            res+="0"+String.valueOf(min);
        else
            res+=String.valueOf(min);
        return res;
    }

    public String getstarttime(int st)
    {
        String res="";
        Date line = new Date((long)st*1000);
        String [] units = String.valueOf(line).split(" ");
        String[] temp = units[3].split(":");
        res = temp[0]+":"+temp[1];
        return res;
    }

    public String getstartdate(int st)
    {
        String res="";
        Date line = new Date((long)st*1000);
        String [] units = String.valueOf(line).split(" ");
        res = units[1] + " " + units[2] + "\n" + units[0];
        return res;
    }
}
