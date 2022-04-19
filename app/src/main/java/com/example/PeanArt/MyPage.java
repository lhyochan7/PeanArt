package com.example.PeanArt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.PeanArt.adapter.ExhibitAdapter;
import com.example.PeanArt.adapter.followedUserAdapter;
import com.example.PeanArt.model.Exhibition;
import com.example.PeanArt.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPage extends Fragment {

    private final String TAG = "MyPage";

    private FirebaseFirestore fs;
    String uid;
    // User 프로필 사진 및 아이디 표시용 view
    private ImageView profileImg;
    private TextView welcome_name;
    // Follow User Recycler View 용 Variable
    ArrayList<User> mUserList;
    RecyclerView rcView_follow;
    followedUserAdapter mFollowAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            uid = bundle.getString("uid");
        }
        uid = "FTScRKFyelcxtPA2u2hN7bA7bJD3";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_my_page, container, false);
        fs = FirebaseFirestore.getInstance();
        profileImg = rootView.findViewById(R.id.mypage_profileImg);
        welcome_name = rootView.findViewById(R.id.mypage_welcome_name);

        LoadUserData();
        LoadFollowedUser();
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
            //TODO: follow 안에 들어있는 userUID 와 documentID 같은놈들 찾기
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            Log.i(TAG, "onComplete: " + ((List<String>)document.get("follow")).toString());
                            fs.collection("test_users").whereIn(FieldPath.documentId(), ((List<String>)document.get("follow"))).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        Log.i(TAG, "onComplete: " + task.getResult().getDocuments().toString());
                                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                                            if(documentSnapshot.exists()){
                                                Log.i(TAG, "found by Array: " + documentSnapshot.get("nickname").toString());
                                            }
                                        }
                                    }else{}
                                }
                            });
                        }
                    }
                }else {

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