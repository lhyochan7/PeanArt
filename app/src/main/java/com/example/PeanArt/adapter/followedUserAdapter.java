package com.example.PeanArt.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.PeanArt.R;
import com.example.PeanArt.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class followedUserAdapter extends RecyclerView.Adapter<followedUserAdapter.ViewHolder> {
    private ArrayList<User> mUserList;
    private StorageReference storageRef;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followeduser, parent, false);
        storageRef = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com/").getReference();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        int cnt = mUserList != null ? mUserList.size() : 0; // userList가 null이면 0, 아니면 리스트 사이즈 반환
        Log.i("UA_TEST", "getItemCount: "+cnt);
        return cnt;
    }

    public void setmUserList(ArrayList<User> list){
        this.mUserList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtView_nickname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.followed_user_img);
            txtView_nickname = itemView.findViewById(R.id.followed_user_nickname);
            Log.i("VIEW TEST", "ViewHolder: " + txtView_nickname.getId());
        }
        void onBind(User user){
            // 추후 User 클래스를 parameter로 받는걸로 바꿔서 그 User에서
            // profile image, nickname 등을 get 해와야 함.
            storageRef.child("User/"+user.getID()+".png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Glide.with(itemView).load(task.getResult()).into(imageView);
                    }
                }
            });
            Log.i("fUA Test", "onBind: " + user.getNickname());
            Log.i("fUA Test", "onBind: " + user.getID());
            txtView_nickname.setText("닉네임: "+ user.getNickname());
        }
    }
}