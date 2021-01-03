package com.example.socialcode;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Problems extends AppCompatActivity {

    private RecyclerView recyclerViewParent;

    ArrayList<ParentChild> parentChildObj;
    JSONObject jsonObject;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);
        getSupportActionBar().setTitle("Problems");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorPrimary));
        }
        else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        recyclerViewParent = (RecyclerView) findViewById(R.id.rv_parent);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        FileCacher<HashMap<String, ArrayList<String>>> cacher = new FileCacher<>(getApplicationContext(), "problems.txt");
        HashMap<String, ArrayList<String>> obj = new HashMap<String, ArrayList<String>>();
        try {
            obj = cacher.readCache();
//            if(!obj.get("image").equals("")){
//                byte[] decodeString = Base64.decode(obj.get("image"), Base64.DEFAULT);
//                Bitmap bitmap_img = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
//                profile_pic.setImageBitmap(bitmap_img);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        parentChildObj = createData(obj);
        ParentAdapter parentAdapter = new ParentAdapter(getApplicationContext(), parentChildObj);
        recyclerViewParent.setAdapter(parentAdapter);
        recyclerViewParent.setLayoutManager(manager);
        recyclerViewParent.setHasFixedSize(true);
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
}
