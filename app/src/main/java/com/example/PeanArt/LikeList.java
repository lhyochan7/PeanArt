package com.example.PeanArt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.PeanArt.adapter.ExhibitAdapter;
import com.example.PeanArt.model.Exhibition;
import com.example.PeanArt.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class LikeList extends Fragment {

    private final String TAG = "LikeList";
    String uid;

    public static FirebaseFirestore fs = FirebaseFirestore.getInstance();
    // view 용 variable
    ArrayList<Exhibition> mExhibitList;
    RecyclerView rcView;
    ExhibitAdapter mExhibitAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            uid = bundle.getString("uid");
        }
        uid = "FTScRKFyelcxtPA2u2hN7bA7bJD3";
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_like_list, container, false);
        mExhibitList = new ArrayList<Exhibition>();
        rcView = rootView.findViewById(R.id.rcView_likedList);
        rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExhibitAdapter = new ExhibitAdapter();
        rcView.setAdapter(mExhibitAdapter);
        LoadLikedList();
        return rootView;
    }
    public void LoadLikedList(){
        fs.collection("test_users").whereEqualTo(FieldPath.documentId(), uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            // 현재 회원이 좋아요 한 전시회 목록
                            List<String> likedExhibition = (List<String>)document.get("liked");
                            Log.i(TAG, "onComplete: " + (likedExhibition).toString());
                            Log.i(TAG, "array: " + Lists.partition(likedExhibition, 10).toString());
                            // 좋아요 한 전시회 목록을 10개 단위로 쪼개서 쿼리에 넣어 실행 ( Array를 이용한 쿼리문(WhereIn)은 한번에 조회할수 있는 Array 크기가 10으로 제한됨.
                            for (List<String> partition : Lists.partition(likedExhibition, 10)){
                                fs.collection("exhibition").whereIn(FieldPath.documentId(), partition).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            Log.i(TAG, "onComplete: " + task.getResult().getDocuments().toString());
                                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                                if(documentSnapshot.exists()){
                                                    Log.i(TAG, "found by Array_ title: " + documentSnapshot.get("title").toString());
                                                    Exhibition tmp = documentSnapshot.toObject(Exhibition.class);
                                                    tmp.setId(documentSnapshot.getId());
                                                    tmp.setLiked(true);
                                                    mExhibitList.add(tmp);
                                                }
                                            }
                                            mExhibitAdapter.setmExhibitList(mExhibitList);
                                            mExhibitAdapter.notifyDataSetChanged();
                                        }else{}
                                    }
                                });
                            }
                        }
                    }
                }else {

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