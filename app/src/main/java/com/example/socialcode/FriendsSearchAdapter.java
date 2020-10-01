package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsSearchAdapter extends RecyclerView.Adapter<FriendsSearchAdapter.SearchViewHolder> {

    Context context;
    ArrayList<FriendsInfo> arrayList;

    public FriendsSearchAdapter(Context context, ArrayList<FriendsInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{
        CircleImageView Image;
        TextView Name,Email;
        LinearLayout ParentLayout;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = (CircleImageView) itemView.findViewById(R.id.friends_image);
            Name = (TextView) itemView.findViewById(R.id.friends_name);
            Email = (TextView) itemView.findViewById(R.id.friends_email);
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
        if(!arrayList.get(i).getProfilepic().equals("")){
            byte[] decodedString = Base64.decode(arrayList.get(i).getProfilepic(), Base64.DEFAULT);
            Bitmap bitmap_img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            searchViewHolder.Image.setImageBitmap(bitmap_img);
        }
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.defaultpic);

        searchViewHolder.ParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,FriendProfile.class);
                intent.putExtra("name", arrayList.get(i).getName());
                intent.putExtra("email", arrayList.get(i).getEmail());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
