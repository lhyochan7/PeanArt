package com.example.PeanArt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // ------------------
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private visitedExhibitImageAdapter mSimpleImageAdapter;
    private UserAdapter mUserAdapter;
    //-------------------
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPageFragment newInstance(String param1, String param2) {
        MyPageFragment fragment = new MyPageFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView =  (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);
        mRecyclerView = rootView.findViewById(R.id.mypage_rcView_visited);
        mSimpleImageAdapter = new visitedExhibitImageAdapter();

        mRecyclerView2 = rootView.findViewById(R.id.mypage_rcView_followed);
        mUserAdapter = new UserAdapter();

        mRecyclerView.setAdapter(mSimpleImageAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView2.setAdapter(mUserAdapter);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        return rootView;
    }
}