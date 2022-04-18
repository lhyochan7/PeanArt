package com.example.PeanArt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class visitedExhibitImageAdapter extends RecyclerView.Adapter<visitedExhibitImageAdapter.ViewHolder> {
    // 추후 방문한 전시회 클래스를 만들거나, 방문한 전시회들의 포스터 사진 URL을 보관하는 객체(ex. String)의 ArrayList로 바꿔줘야 함
    private ArrayList<Integer> mImageIdList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mImageIdList.get(position));
    }

    public void setmImageIdList(ArrayList<Integer> list){
        this.mImageIdList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mImageIdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.visited_exhibit_posterImg);
        }
        void onBind(Integer id){
            imageView.setImageResource(id);
        }
    }
}
class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    // 추후 User 클래스 만들어서 User 클래스의 ArrayList로 바꿔줘야 함
    private ArrayList<Integer> mUserList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followeduser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setmUserList(ArrayList<Integer> list){
        this.mUserList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.followed_user_img);
            txtView = itemView.findViewById(R.id.followed_uesr_nickname);
        }
        void onBind(Integer id){
            // 추후 User 클래스를 parameter로 받는걸로 바꿔서 그 User에서
            // profile image, nickname 등을 get 해와야 함.
            imageView.setImageResource(id);
            txtView.setText("닉네임: "+id);
        }
    }
}