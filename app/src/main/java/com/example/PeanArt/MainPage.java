package com.example.PeanArt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PeanArt.adapter.ExhibitAdapter;
import com.example.PeanArt.model.Exhibition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainPage extends Fragment {
    private String TAG = "MainPage";
    String uid;
    private FirebaseFirestore fs;
    // view 용 variable
    ArrayList<Exhibition> mExhibitList;
    RecyclerView rcView;
    ExhibitAdapter mExhibitAdapter;

    public MainPage() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            uid = bundle.getString("uid");
        }
        fs = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_main_recycler, container, false);
        mExhibitList = new ArrayList<Exhibition>();
        rcView = rootView.findViewById(R.id.rcView_main);
        rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExhibitAdapter = new ExhibitAdapter();
        rcView.setAdapter(mExhibitAdapter);
        setRecyclerview();
        return rootView;
    }
    public void setRecyclerview(){
        fs.collection("exhibition").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            Log.i(TAG, document.get("detail").toString());
                            Exhibition tmp = document.toObject(Exhibition.class);
                            Log.i(TAG, "Before SetID: " + tmp.getId());
                            tmp.setId(document.getId()); // 별도로 ID ( Exhibition의 Document ID ) 추가.
                            Log.i(TAG, "Get Exhibition ID : " + document.getId());
                            mExhibitList.add(tmp);
                        }
                    }
                    mExhibitAdapter.setmExhibitList(mExhibitList);
                    mExhibitAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "Failed with: "+task.getException());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "RESUME UID : " + uid);
    }
}