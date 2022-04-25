package com.example.PeanArt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.PeanArt.adapter.ExhibitAdapter;
import com.example.PeanArt.adapter.followedUserAdapter;
import com.example.PeanArt.adapter.visitedExhibitAdapter;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MyPage extends Fragment {

    private final String TAG = "MyPage";

    private FirebaseFirestore fs;
    String uid;
    // User 프로필 사진 및 아이디 표시용 view
    private ImageView profileImg;
    private TextView welcome_name;
    Button btn_regist_exhibition, btn_logout;
    // Follow User Recycler View 용 Variable
    ArrayList<User> mUserList;
    RecyclerView rcView_follow;
    followedUserAdapter mFollowAdapter;
    // Visited Exhibition Recycler View 용 Variable
    ArrayList<String> mExhibitionIdList;
    RecyclerView rcView_visited;
    visitedExhibitAdapter mVisitedAdapter;
    private final int REGISTER_CODE = 878787;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            uid = bundle.getString("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_my_page, container, false);
        fs = FirebaseFirestore.getInstance();
        profileImg = rootView.findViewById(R.id.mypage_profileImg);
        welcome_name = rootView.findViewById(R.id.mypage_welcome_name);
        btn_regist_exhibition = rootView.findViewById(R.id.btn_regist_exhibition);
        btn_logout = rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                startActivity(new Intent(getActivity(), Login.class));
            }
        });
        mUserList = new ArrayList<User>();
        mFollowAdapter = new followedUserAdapter();
        rcView_follow = rootView.findViewById(R.id.mypage_rcView_followed);
        rcView_follow.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcView_follow.setAdapter(mFollowAdapter);

        mExhibitionIdList = new ArrayList<>();
        mVisitedAdapter = new visitedExhibitAdapter();
        rcView_visited = rootView.findViewById(R.id.mypage_rcView_visited);
        rcView_visited.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcView_visited.setAdapter(mVisitedAdapter);

        btn_regist_exhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uidIntent = new Intent(getActivity(), RegisterGallery.class);
                uidIntent.putExtra("uid", uid);
                startActivity(uidIntent);
            }
        });

        LoadUserData();
        LoadFollowedUser();
        LoadVisitedExhibition();
        return rootView;
    }
    public void LoadUserData(){
        fs.collection("test_users").whereEqualTo(FieldPath.documentId(), uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            Log.i(TAG, document.get("nickname").toString());
                            User tmp = document.toObject(User.class);
                            tmp.setID(document.getId());
                            Log.i(TAG, "onComplete: "+ tmp.getID());
                            SetProfileImg(tmp);
                            welcome_name.setText(tmp.getNickname()+" 님 반갑습니다!");
                        }
                    }
                } else{
                    Log.e(TAG, "onComplete: ", task.getException());
                }
            }
        });
    }
    public void LoadFollowedUser(){
        fs.collection("test_users").whereEqualTo(FieldPath.documentId(), uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            // 현재 회원이 팔로우한 유저 목록
                            List<String> follwingUsers = (List<String>)document.get("follow");
                            Log.i(TAG, "onComplete: " + (follwingUsers).toString());
                            Log.i(TAG, "array: " + Lists.partition(follwingUsers, 10).toString());
                            // 팔로우한 계정 목록을 10개 단위로 쪼개서 쿼리에 넣어 실행 ( Array를 이용한 쿼리문(WhereIn)은 한번에 조회할수 있는 Array 크기가 10으로 제한됨.
                            for (List<String> partition : Lists.partition(follwingUsers, 10)){
                                fs.collection("test_users").whereIn(FieldPath.documentId(), partition).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            Log.i(TAG, "onComplete: " + task.getResult().getDocuments().toString());
                                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                                if(documentSnapshot.exists()){
                                                    Log.i(TAG, "found by Array_ nickname: " + documentSnapshot.get("nickname").toString());
                                                    User tmp = documentSnapshot.toObject(User.class);
                                                    tmp.setID(documentSnapshot.getId());
                                                    mUserList.add(tmp);
                                                }
                                            }
                                            mFollowAdapter.setmUserList(mUserList);
                                            mFollowAdapter.notifyDataSetChanged();
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

    public void LoadVisitedExhibition(){
        fs.collection("review").whereEqualTo("writerID", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            // 현재 회원이 방문한 ( 리뷰남긴 ) 전시회 목록
                            String visitedExhibitionID = (String) document.get("exhibitionID");
                            Log.i(TAG, "visited Exhibition: " + visitedExhibitionID);
                            if(!mExhibitionIdList.contains(visitedExhibitionID))
                                mExhibitionIdList.add(visitedExhibitionID);
                            }
                        }
                    mVisitedAdapter.setmVisitedExhibitList(mExhibitionIdList);
                    mVisitedAdapter.notifyDataSetChanged();
                    }
                else {
                }
            }
        });
    }
    public void SetProfileImg(User user){
        StorageReference storageRef = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com/").getReference();
        storageRef.child("User/"+user.getID()+".png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Glide.with(getView()).load(task.getResult()).into(profileImg);
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