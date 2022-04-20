package com.example.PeanArt.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.PeanArt.R;
import com.example.PeanArt.model.User;

import java.util.ArrayList;

public class followedUserAdapter extends RecyclerView.Adapter<followedUserAdapter.ViewHolder> {
    private ArrayList<User> mUserList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
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
        TextView txtView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // imageView = itemView.findViewById(R.id.followed_user_img);
            // txtView = itemView.findViewById(R.id.followed_uesr_nickname);
        }
        void onBind(User user){
            // 추후 User 클래스를 parameter로 받는걸로 바꿔서 그 User에서
            // profile image, nickname 등을 get 해와야 함.
            txtView.setText("닉네임: "+ user.getNickname());
        }
    }
}