package com.example.PeanArt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MyPage extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private visitedExhibitImageAdapter mSimpleImageAdapter;
    private UserAdapter mUserAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mRecyclerView = findViewById(R.id.mypage_rcView_visited);
        mSimpleImageAdapter = new visitedExhibitImageAdapter();

        mRecyclerView2 = findViewById(R.id.mypage_rcView_followed);
        mUserAdapter = new UserAdapter();

        mRecyclerView.setAdapter(mSimpleImageAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView2.setAdapter(mUserAdapter);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));

        ArrayList mImageIds = new ArrayList<>();
        mImageIds.add(R.drawable.img2);
        mImageIds.add(R.drawable.img3);
        mImageIds.add(R.drawable.kiss);

        mSimpleImageAdapter.setmImageIdList(mImageIds);

        ArrayList mUserList = new ArrayList<>();
        mUserList.add(R.drawable.profileicon);
        mUserList.add(R.drawable.img2);
        mUserList.add(R.drawable.img3);
        mUserList.add(R.drawable.img3);
        mUserList.add(R.drawable.img3);

        mUserAdapter.setmUserList(mUserList);
    }
}