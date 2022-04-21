package com.example.PeanArt.adapter;

import android.content.Context;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;

    int i=0;


    private final static String TAG = "DetailAdaptor";

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView detailIMG;
        StorageReference storageRef;

        ViewHolder(View itemView) {
            super(itemView) ;
            storageRef = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com/").getReference();
            detailIMG = itemView.findViewById(R.id.detailIMG);
            // 뷰 객체에 대한 참조. (hold strong reference)
            //textView1 = itemView.findViewById(R.id.text1) ;
        }

        public void onBind(String s) {

            Log.i(TAG, mData.get(i));
            i++;
            storageRef.child("Exhibition/"+mData.get(i-1)+".png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Glide.with(itemView).load(task.getResult()).into(detailIMG);
                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public DetailAdapter(ArrayList<String> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_img, parent, false);
        DetailAdapter.ViewHolder vh = new DetailAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(DetailAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
