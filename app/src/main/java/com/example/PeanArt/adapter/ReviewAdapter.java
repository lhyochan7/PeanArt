package com.example.PeanArt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PeanArt.R;
import com.example.PeanArt.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> mReviewList;
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_review, parent, false);

        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.onBind(mReviewList.get(position));
    }
    public void setmReviewList(ArrayList<Review> list){
        this.mReviewList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        int cnt = mReviewList != null ? mReviewList.size() : 0;
        return cnt;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView TxtNickname, TxtContent, TxtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtNickname = itemView.findViewById(R.id.txt_review_nickname);
            TxtContent = itemView.findViewById(R.id.txt_review_content);
            TxtDate = itemView.findViewById(R.id.txt_review_date);
        }
        void onBind(Review review){
            fs.collection("test_users").whereEqualTo(FieldPath.documentId(), review.getWriterID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot document : task.getResult()){
                            TxtNickname.setText((String)document.get("nickname"));
                        }
                    }
                }
            });
            TxtContent.setText(review.getContent());
            TxtDate.setText(review.getWriteDate());
        }
    }
}
