package com.example.PeanArt.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.PeanArt.R;
import com.example.PeanArt.model.Exhibition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ExhibitAdapter extends RecyclerView.Adapter<ExhibitAdapter.ViewHolder> {
    private ArrayList<Exhibition> mExhibitList;

    public interface OnExhibitionSelectedListener {
        void onExhibitionSelectedListener(DocumentSnapshot exhibition);
    }
    private OnExhibitionSelectedListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exhibit_cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mExhibitList.get(position));
    }

    public void setmExhibitList(ArrayList<Exhibition> list){
        this.mExhibitList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        // ExhibitList가 null, 즉 아직 불러오지 못한 상태면 0 반환 -> 아무것도 표시 안됨.
        int cnt = mExhibitList != null ? mExhibitList.size() : 0;
        return cnt;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImg;
        TextView cardTitle, cardInfo, txt_ed, txt_sd, txt_geo;
        CardView cardView;
        StorageReference storageRef;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 접근할 firebase storage url setting
            storageRef = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com/").getReference();
            cardView = itemView.findViewById(R.id.cardView);
            cardImg = itemView.findViewById(R.id.IMG_card_exhibit);
            cardTitle = itemView.findViewById(R.id.txt_card_exhibit_title);
            cardInfo = itemView.findViewById(R.id.txt_card_exhibit_info);
            // txt_sd = itemView.findViewById(R.id.txt_startDate);
            // txt_ed = itemView.findViewById(R.id.txt_endDate);
            // txt_geo = itemView.findViewById(R.id.txt_geoPoint);
        }
        void onBind(Exhibition exhibition){
            // cardImg Set
            storageRef.child("exhibition/"+exhibition.getId()+"/poster.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Glide.with(itemView).load(task.getResult()).into(cardImg);
                    }
                }
            });
            cardTitle.setText(exhibition.getTitle());
            cardInfo.setText(exhibition.getInfo());
            // txt_sd.setText(exhibition.getStartDate().toString());
            // txt_ed.setText(exhibition.getEndDate().toString());
            // txt_geo.setText(exhibition.getLocation().toString());
        }
    }
}
