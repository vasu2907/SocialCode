package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FriendsSearchAdapter extends RecyclerView.Adapter<FriendsSearchAdapter.SearchViewHolder> {

    Context context;
    ArrayList<FriendsInfo> arrayList;
    StorageReference storageReference;
    DatabaseReference myref;

    public FriendsSearchAdapter(Context context, ArrayList<FriendsInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{

        ImageView Image,addfriend;
        TextView Name,Email;
        LinearLayout ParentLayout;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = (ImageView) itemView.findViewById(R.id.friends_image);
            Name = (TextView) itemView.findViewById(R.id.friends_name);
            Email = (TextView) itemView.findViewById(R.id.friends_email);
            addfriend = (ImageView) itemView.findViewById(R.id.friends_addfriend);
            ParentLayout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
        }
    }

    @NonNull
    @Override
    public FriendsSearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.searchfriends_layout,viewGroup,false);
        return new FriendsSearchAdapter.SearchViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final FriendsSearchAdapter.SearchViewHolder searchViewHolder, final int i) {
        searchViewHolder.Name.setText(arrayList.get(i).getName());
        searchViewHolder.Email.setText(arrayList.get(i).getEmail());
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.defaultpic);
        storageReference = FirebaseStorage.getInstance().getReference();
        myref = FirebaseDatabase.getInstance().getReference("Users");

        storageReference.child("profilepics/"+arrayList.get(i).getProfilepic()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).setDefaultRequestOptions(requestOptions)
                        .asBitmap().load(uri).into(searchViewHolder.Image);
            }
        });

        searchViewHolder.ParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,FriendProfile.class);
                intent.putExtra("codeforces_handle",arrayList.get(i).getCodeforces_handle());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        searchViewHolder.addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for( DataSnapshot snapshot : dataSnapshot.getChildren())
                        {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
