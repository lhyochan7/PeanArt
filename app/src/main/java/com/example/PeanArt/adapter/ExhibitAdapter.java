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
import com.example.PeanArt.model.Exhibition;
import com.google.firebase.firestore.DocumentSnapshot;

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
        int cnt = mExhibitList != null ? mExhibitList.size() : 0;
        Log.i("EA_TAG", "getItemCount: "+cnt);
        return cnt;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImg;
        TextView cardTitle, cardInfo, txt_ed, txt_sd, txt_geo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardImg = itemView.findViewById(R.id.IMG_card_exhibit);
            cardTitle = itemView.findViewById(R.id.txt_card_exhibit_title);
            cardInfo = itemView.findViewById(R.id.txt_card_exhibit_info);
            // txt_sd = itemView.findViewById(R.id.txt_startDate);
            // txt_ed = itemView.findViewById(R.id.txt_endDate);
            // txt_geo = itemView.findViewById(R.id.txt_geoPoint);
        }
        void onBind(Exhibition exhibition){
            // cardImg Set
            cardTitle.setText(exhibition.getTitle());
            cardInfo.setText(exhibition.getInfo());
            // txt_sd.setText(exhibition.getStartDate().toString());
            // txt_ed.setText(exhibition.getEndDate().toString());
            // txt_geo.setText(exhibition.getLocation().toString());
        }
    }
}
