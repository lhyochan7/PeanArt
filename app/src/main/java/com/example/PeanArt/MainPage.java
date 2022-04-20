package com.example.PeanArt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private int searchTrigger;
    // 상단 버튼
    private Button btn_main_all, btn_main_coll, btn_main_inde, btn_main_etc;
    private View.OnClickListener kindsClickListener;

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
        searchTrigger = 0;
        uid = "FTScRKFyelcxtPA2u2hN7bA7bJD3";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main_recycler, container, false);
        mExhibitList = new ArrayList<Exhibition>();
        rcView = rootView.findViewById(R.id.rcView_main);
        rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExhibitAdapter = new ExhibitAdapter();
        rcView.setAdapter(mExhibitAdapter);

        btn_main_all = rootView.findViewById(R.id.btn_main_all);
        btn_main_coll = rootView.findViewById(R.id.btn_main_coll);
        btn_main_inde = rootView.findViewById(R.id.btn_main_inde);
        btn_main_etc = rootView.findViewById(R.id.btn_main_etc);

        kindsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_main_all:
                        searchTrigger = 0;
                        break;
                    case R.id.btn_main_coll:
                        searchTrigger = 1;
                        break;
                    case R.id.btn_main_inde:
                        searchTrigger = 2;
                        break;
                    case R.id.btn_main_etc:
                        searchTrigger = 3;
                        break;
                }
                setRecyclerview();
            }
        };
        btn_main_all.setOnClickListener(kindsClickListener);
        btn_main_coll.setOnClickListener(kindsClickListener);
        btn_main_inde.setOnClickListener(kindsClickListener);
        btn_main_etc.setOnClickListener(kindsClickListener);

        setRecyclerview();
        return rootView;
    }
    public void setRecyclerview(){
        Task<QuerySnapshot> res;
        mExhibitList.clear();
        //SearchTrigger => 0 : 전체 / 1 : 대학 전시회 / 2: 개인 전시회 / 3 : 기타
        switch (searchTrigger){
            case 1:
                res = fs.collection("exhibition").whereEqualTo("kind", 1).get();
                break;
            case 2:
                res = fs.collection("exhibition").whereEqualTo("kind", 2).get();
                break;
            case 3:
                res = fs.collection("exhibition").whereEqualTo("kind", 3).get();
                break;
            default:
                res = fs.collection("exhibition").get();
                break;
        }
        fs.collection("test_users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        ArrayList<String> likedList = (ArrayList<String>) task.getResult().get("liked");
                        res.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (DocumentSnapshot document : task.getResult()){
                                        if(document.exists()){
                                            Log.i(TAG, document.get("detail").toString());
                                            Exhibition tmp = document.toObject(Exhibition.class);
                                            Log.i(TAG, "Before SetID: " + tmp.getId());
                                            tmp.setId(document.getId()); // 별도로 ID ( Exhibition의 Document ID ) 추가.
                                            if(likedList.contains(tmp.getId())) tmp.setLiked(true);
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
                }else{

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