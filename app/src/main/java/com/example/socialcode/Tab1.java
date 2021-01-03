package com.example.socialcode;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment {


    public Tab1() {
        // Required empty public constructor
    }

    private RecyclerView recyclerViewParent;

    ArrayList<ParentChild> parentChildObj;
    JSONObject jsonObject;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);


        return view;
    }

    

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        MyTask obj =  new MyTask(view);
        obj.execute();
//        parentChildObj = obj.get_object();



    }

    private class MyTask extends AsyncTask<Void, Void, Void>{
        private View view;

        public MyTask(View view) {
            super();
            this.view = view;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerViewParent = (RecyclerView) view.findViewById(R.id.rv_parent);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewParent.setLayoutManager(manager);
            recyclerViewParent.setHasFixedSize(true);

//            ParentAdapter parentAdapter = new ParentAdapter(getContext(), parentChildObj);
//            recyclerViewParent.setAdapter(parentAdapter);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<JSONObject> call = jsonPlaceHolderApi.get_problems("vasu2907");
            Response<JSONObject> response = null;
            try {
                response = call.execute();
                jsonObject = response.body();
                parentChildObj = createData(jsonObject);
                ParentAdapter parentAdapter = new ParentAdapter(getContext(), parentChildObj);
                recyclerViewParent.setAdapter(parentAdapter);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private ArrayList<ParentChild> createData(JSONObject jsonObject) {
        parentChildObj = new ArrayList<>();
        if( jsonObject == null){
            return parentChildObj;
        }
        try{

//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//            Call<JSONObject> call = jsonPlaceHolderApi.get_problems("vasu2907");
//            Response<JSONObject> response = call.execute();
//            jsonObject = response.body();



            for(int i = 0; i< jsonObject.names().length(); i++){
                String category = jsonObject.names().getString(i);
                JSONArray jsonArray = jsonObject.getJSONArray(category);
                ParentChild object = new ParentChild();
                ArrayList<Child> list = new ArrayList<>();
                Child c = new Child();
                c.setChild_name(category);
                list.add(c);
                for(int j=0;j<jsonArray.length();j++){
                    Child ch = new Child();
                    ch.setChild_name(jsonArray.getString(j));
                    list.add(ch);
                }
                object.setChild(list);
                parentChildObj.add(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parentChildObj;
    }

}
