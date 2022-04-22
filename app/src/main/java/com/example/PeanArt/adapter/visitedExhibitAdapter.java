package com.example.PeanArt.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.PeanArt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class visitedExhibitAdapter extends RecyclerView.Adapter<visitedExhibitAdapter.ViewHolder>{
    private ArrayList<String> mVisitedExhibitList;
    StorageReference storageRef;
    @NonNull
    @Override
    public visitedExhibitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visited_exhibition, parent, false);
        storageRef = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com").getReference();
        return new visitedExhibitAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull visitedExhibitAdapter.ViewHolder holder, int position) {
        holder.onBind(mVisitedExhibitList.get(position));
    }
    public void setmVisitedExhibitList(ArrayList<String> list){
        this.mVisitedExhibitList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        int cnt = mVisitedExhibitList != null ? mVisitedExhibitList.size() : 0;
        return cnt;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView visitedExhibitImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            visitedExhibitImg = itemView.findViewById(R.id.visitedExhibitImg);
        }
        void onBind(String exhibitionID){
            Log.i("vEA_ TEST", "vEA onBind: "+exhibitionID);
            storageRef.child("Exhibition/"+exhibitionID+"/poster.png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Glide.with(itemView).load(task.getResult()).into(visitedExhibitImg);
                    }
                }
            });
        }
    }
}
