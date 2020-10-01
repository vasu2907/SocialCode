package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.caching.FileCacher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateProfile extends AppCompatActivity {
    private EditText Name,College,Codechef,Codeforces,Hackerrank;
    private TextView Email, User_verification_msg;
    private ImageView profilepic, retrieve_data, verified_user;
    private Button save;
    private String profileImageurl;
    private Uri uriprofileimage;
    private static final int choose_Image =101;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private DynamoUserInfo info;
//    private FirebaseAuth auth;
//    private DatabaseReference myref;
//    private FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorPrimary));
        }
        else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
//        Toast.makeText(getApplicationContext(), "ONCREATE", Toast.LENGTH_LONG).show();
        Name = (EditText) findViewById(R.id.updateprofile_name);
        College = (EditText) findViewById(R.id.updateprofile_college);
        Codechef = (EditText) findViewById(R.id.updateprofile_codechef);
        Codeforces = (EditText) findViewById(R.id.updateprofile_codeforces);
        Hackerrank = (EditText) findViewById(R.id.updateprofile_hackerrank);
        profilepic = (ImageView) findViewById(R.id.updateprofile_img);
        save = (Button) findViewById(R.id.updateprofile_save);
        Email = (TextView) findViewById(R.id.updateprofile_email);
        retrieve_data = (ImageView) findViewById(R.id.retrieve_data);
        verified_user = (ImageView) findViewById(R.id.verify_user);
        User_verification_msg = (TextView) findViewById(R.id.verify_msg);

        String base64 = getCachedImage();
        if(!base64.equals("")){
            byte[] decodeString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bitmap_img = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
            profilepic.setImageBitmap(bitmap_img);
            profilepic.setTag("profilepic");
            Toast.makeText(getApplicationContext(), "hbe", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String verified = sharedPref.getString("verified", "False");

        if(verified.equals("True") == true){
            verified_user.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.verified_user));
            User_verification_msg.setText("Verified");
        }

//        auth = FirebaseAuth.getInstance();
//        firebaseStorage = FirebaseStorage.getInstance();
//        myref = FirebaseDatabase.getInstance().getReference("Users");
//        profilepic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showImageChooser();
//            }
//        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  saveUpdatedInfo();
//                saveUserInfo();
//                UserInfo newInfo = new UserInfo(Name.getText().toString(),College.getText().toString(),
//                                                Email.getText().toString(),Codeforces.getText().toString(),
//                                                Codechef.getText().toString(),Hackerrank.getText().toString());
//                myref.child(auth.getCurrentUser().getUid()).child("Info").setValue(newInfo);
            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        retrieve_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieve_userInfo();
                update_SharedPrefData();
            }
        });

        verified_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_user();
            }
        });
    }

    private void verify_user(){
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        String verified = sharedPref.getString("verified", "False");
        if(verified.equals("True") == true){
            Toast.makeText(getApplicationContext(), "User Already Verified", Toast.LENGTH_SHORT).show();
            return ;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        VerifyUserBody body = new VerifyUserBody(email);
        Call<RegisterAPIResponse> call = jsonPlaceHolderApi.verify_user(body);
        call.enqueue(new Callback<RegisterAPIResponse>() {
            @Override
            public void onResponse(Call<RegisterAPIResponse> call, Response<RegisterAPIResponse> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Email not Sent", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RegisterAPIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Email not Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update_SharedPrefData(){
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<DynamoUserInfo> call = jsonPlaceHolderApi.getUserInfo(email);
        call.enqueue(new Callback<DynamoUserInfo>() {
            @Override
            public void onResponse(Call<DynamoUserInfo> call, Response<DynamoUserInfo> response) {
                if(!response.isSuccessful()) {
                    return;
                }
                DynamoUserInfo postResponse = response.body();
                HashMap<String, String> codeforces = postResponse.getCodeforces();
                SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPref.edit();
                editor.putString("codeforces_rating", codeforces.get("rating"));
                editor.putString("codeforces_friends", codeforces.get("friends"));
                editor.putString("codeforces_contest", codeforces.get("contests"));
                editor.apply();
            }

            @Override
            public void onFailure(Call<DynamoUserInfo> call, Throwable t) {
            }
        });
    }

    private void retrieve_userInfo(){
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<RetrieveDataResponse> call = jsonPlaceHolderApi.retrieveData(email);
        call.enqueue(new Callback<RetrieveDataResponse>() {
            @Override
            public void onResponse(Call<RetrieveDataResponse> call, Response<RetrieveDataResponse> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Unable to Retrieve data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Retrieved Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RetrieveDataResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to Retrieve Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean hasImage(@NonNull ImageView view){
        if(String.valueOf(view.getTag()).equals("bg")){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private String getCachedImage(){
        FileCacher<HashMap<String, String>> cacher = new FileCacher<>(getApplicationContext(), "user.txt");
        HashMap<String, String> obj = null;
        String base64 = "";
        try {
            obj = cacher.readCache();
            base64 = obj.get("image");
        } catch (IOException e) {
            e.printStackTrace();
            base64 = "";
        }
        return base64;
    }

    private void updateCache(@NonNull ImageView view){
        if (!hasImage(view)){
            return ;
        }
        FileCacher<HashMap<String, String>> cacher = new FileCacher<>(getApplicationContext(), "user.txt");
        HashMap<String, String> obj = new HashMap<String, String>();
        BitmapDrawable drawable = (BitmapDrawable) profilepic.getDrawable();
        Bitmap bitmap_img = drawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap_img.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        String base64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        obj.put("image", base64);
        try {
            cacher.writeCache(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUpdatedInfo(){
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        String name = Name.getText().toString();
        String codechef = Codechef.getText().toString();
        String codeforces = Codeforces.getText().toString();
        String hackerrank = Hackerrank.getText().toString();
        String college = College.getText().toString();
        String base64 = "";
        if(hasImage(profilepic)){
            BitmapDrawable drawable = (BitmapDrawable) profilepic.getDrawable();
            Bitmap bitmap_img = drawable.getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap_img.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
            base64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        }
//        Toast.makeText(getApplicationContext(), "image:"+hasImage(profilepic).toString(), Toast.LENGTH_SHORT).show();

        SaveProfileBody body = new SaveProfileBody(name, email, college, codechef, codeforces, hackerrank, base64);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<SaveProfileResponse> call = jsonPlaceHolderApi.saveProfileData(email, body);

        call.enqueue(new Callback<SaveProfileResponse>() {
            @Override
            public void onResponse(Call<SaveProfileResponse> call, Response<SaveProfileResponse> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Unable to Update data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Data updated Successfully", Toast.LENGTH_SHORT).show();
                updateCache(profilepic);
            }

            @Override
            public void onFailure(Call<SaveProfileResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to Update Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void saveUserInfo()
//    {
//        FirebaseUser user = auth.getCurrentUser();
//        if(user!=null && profileImageurl!=null)
//        {
//            UserProfileChangeRequest profileChangeRequest =new UserProfileChangeRequest.Builder()
//                    .setDisplayName(Name.getText().toString())
//                    .setPhotoUri(Uri.parse(profileImageurl))
//                    .build();
//            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful())
//                    {
//                        Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),"Some Error Occurred",Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == choose_Image && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriprofileimage =data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uriprofileimage);
                profilepic.setImageBitmap(bitmap);
                profilepic.setTag("profilepic");
//                Toast.makeText(getApplicationContext(), "fheb", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(getApplicationContext(), "ONSTART", Toast.LENGTH_LONG).show();
        Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("https://msfspmx7o8.execute-api.ap-south-1.amazonaws.com/prod/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        Call<DynamoUserInfo> call = jsonPlaceHolderApi.getUserInfo(email);
        call.enqueue(new Callback<DynamoUserInfo>() {
            @Override
            public void onResponse(Call<DynamoUserInfo> call, Response<DynamoUserInfo> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Unable to fetch data", Toast.LENGTH_LONG).show();
                    return;
                }
                info = response.body();
                HashMap<String, String> codeforces = info.getCodeforces();
                HashMap<String, String> codechef = info.getCodechef();
                HashMap<String, String> hackerrank = info.getHackerrank();
                Name.setText(info.getName());
                Email.setText(info.getEmail());
                College.setText(info.getCollege());
                Codechef.setText(codechef.get("handle"));
                Hackerrank.setText(hackerrank.get("handle"));
                Codeforces.setText(codeforces.get("handle"));
                String verified = info.getVerified()?"True":"False";
                SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPref.edit();
                editor.putString("verified", verified);
                editor.commit();
                if(verified.equals("True") == true){
                    verified_user.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.verified_user));
                    User_verification_msg.setText("Verified");
                }else{
                    verified_user.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.not_verified_user));
                    User_verification_msg.setText("Not Verified");
                }
            }

            @Override
            public void onFailure(Call<DynamoUserInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch data", Toast.LENGTH_LONG).show();
            }
        });
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UserInfo userInfo =dataSnapshot.child(auth.getCurrentUser().getUid()).child("Info").getValue(UserInfo.class);
//                Name.setText(userInfo.getName());
//                College.setText(userInfo.getCollege());
//                Codechef.setText(userInfo.getCodechef());
//                Codeforces.setText(userInfo.getCodeforces());
//                Hackerrank.setText(userInfo.getHackerrank());
//                Email.setText(userInfo.getEmail());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(),"Some error occurred",Toast.LENGTH_LONG).show();
//            }
//        });

//        try {
//            firebaseStorage.getReference().child("profilepics/"+auth.getCurrentUser().getUid()+".jpg").getDownloadUrl()
//                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            Glide.with(getApplicationContext()).load(uri).into(profilepic);
////                            profileImageurl = uri.toString();
////                            Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_LONG).show();
//                        }
//                    });
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//            return;
//        }
    }


    private void showImageChooser()
    {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Profile image"),choose_Image);
    }

//    private void uploadImagetoFirebase()
//    {
//        final StorageReference profileImageref = FirebaseStorage.getInstance().getReference("profilepics/" +
//                auth.getCurrentUser().getUid()+".jpg");
//        if(uriprofileimage!=null)
//        {
//            profileImageref.putFile(uriprofileimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    profileImageurl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
////                    Toast.makeText(getApplicationContext(),profileImageurl,Toast.LENGTH_LONG).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    }
}
