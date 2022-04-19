package com.example.PeanArt;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.PeanArt.model.Exhibition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExhibitionDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExhibitionDetailFragment extends Fragment {

    private static final String TAG = "Detail";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String detailTitle, detailPoster, detailDesc;
    private TextView detailTitleTXT, detailDateTXT, detailDescTXT;
    private Date startDate, endDate;
    private Serializable detailExhibition;
    StorageReference storageRef;
    ImageView exhibit_detail_posterImg;

    public ExhibitionDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExhibitionDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExhibitionDetailFragment newInstance(String param1, String param2) {
        ExhibitionDetailFragment fragment = new ExhibitionDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            detailExhibition = getArguments().getSerializable("exhibition");
            Log.i(TAG, "onCreate: " + ((Exhibition)detailExhibition).getId());
            detailTitle = ((Exhibition) detailExhibition).getTitle();
            detailPoster = getArguments().getString("poster");
            startDate = ((Exhibition) detailExhibition).getStartDate();
            endDate = ((Exhibition) detailExhibition).getEndDate();
            detailDesc = ((Exhibition) detailExhibition).getDetail();
        }
        Log.i(TAG, "Get Exhibition : " + detailTitle + detailDesc);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_exhibition_detail, container, false);
        detailTitleTXT = rootView.findViewById(R.id.exhibit_detail_title);
        detailDateTXT = rootView.findViewById(R.id.exhibit_detail_date);
        detailDescTXT = rootView.findViewById(R.id.exhibit_detail_descript);

        detailTitleTXT.setText(detailTitle);
        detailDateTXT.setText(startDate + " ~ " + endDate);
        detailDescTXT.setText(detailDesc);

        exhibit_detail_posterImg = (ImageView)rootView.findViewById(R.id.exhibit_detail_posterImg);

        storageRef = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com/").getReference();
        storageRef.child("Exhibition/"+((Exhibition)detailExhibition).getId()+"/poster.png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Glide.with(rootView).load(task.getResult()).into(exhibit_detail_posterImg);
                }
            }
        });
        return rootView;
    }
}