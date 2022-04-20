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
    // 현재 로그인한 회원의 UID로 회원 객체 찾음. 그 안에 column인 liked (행렬) 찾아서 그 안에 있는 전시회 ID에 해당하는 전시회들 객체 찾아온 뒤 recycler view Adapter에 적재
    public void LoadLikedList(){
        fs.collection("test_users").whereEqualTo(FieldPath.documentId(), uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            // 현재 회원이 좋아요 한 전시회 목록
                            List<String> likedExhibition = (List<String>)document.get("liked");
                            // liked 필드가 없을경우 ( or liked 필드가 비어있을 경우 ) 아무것도 안보여줌
                            boolean isLikedListEmpty = likedExhibition==null;
                            if(!isLikedListEmpty){
                                Log.i(TAG, "isLikedListEmpty" + isLikedListEmpty);
                                Log.i(TAG, "partition array: " + Lists.partition(likedExhibition, 10).toString());
                                // 좋아요 한 전시회 목록을 10개 단위로 쪼개서 쿼리에 넣어 실행 ( Array를 이용한 쿼리문(WhereIn)은 한번에 조회할수 있는 Array 크기가 10으로 제한됨. )
                                for (List<String> partition : Lists.partition(likedExhibition, 10)){
                                    fs.collection("exhibition").whereIn(FieldPath.documentId(), partition).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                Exhibition tmp = new Exhibition();
                                                for(DocumentSnapshot documentSnapshot : task.getResult()){
                                                    if(documentSnapshot.exists()){
                                                        // 전시회 내용이 비어있으면 표시하지 않고 다음 전시회 불러옴
                                                        if(documentSnapshot.getData().size() == 0){
                                                            Log.i(TAG, "document is empty!");
                                                            continue;
                                                        }
                                                        // RecyclerView Adapter용 list에 적재.
                                                        tmp = documentSnapshot.toObject(Exhibition.class);
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