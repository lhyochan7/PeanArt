package com.example.PeanArt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.PeanArt.model.Exhibition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link recommendationResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class recommendationResultFragment extends Fragment {
    private ImageView topThree1, topThree2, topThree3;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    public static FirebaseFirestore fs = FirebaseFirestore.getInstance();


    private ArrayList<String> idList;

    String topThree[];

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public recommendationResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recommendationResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static recommendationResultFragment newInstance(String param1, String param2) {
        recommendationResultFragment fragment = new recommendationResultFragment();
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
            topThree = (String[]) getArguments().get("topThree");
        }
        storage = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com/");
        storageRef = storage.getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_recommendation_result, container, false);
        topThree1 = (ImageView) rootView.findViewById(R.id.topThree1);
        topThree2 = (ImageView) rootView.findViewById(R.id.topThree2);
        topThree3 = (ImageView) rootView.findViewById(R.id.topThree3);


        Log.i("MainActivity2", "MAIN 2 -> Recieved" + topThree[1]);
        ImageView[] viewList = {topThree1, topThree2, topThree3};
        for (int i = 0; i < topThree.length; i++) {
            String path = topThree[i];
            int finalI = i;

            Log.i("MainActivity2", "TOP THREE ID = " + path);

            String arr[] = path.split("/");

            String id = arr[2];

            idList.add(id);


            Log.i("MainActivigy2", "ID = " + id);

            storageRef.child(path).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        switch (finalI) {
                            case 0:
                                Glide.with(getView()).load(task.getResult()).into(topThree1);
                                break;

                            case 1:
                                Glide.with(getView()).load(task.getResult()).into(topThree2);
                                break;

                            case 2:
                                Glide.with(getView()).load(task.getResult()).into(topThree3);
                                break;
                        }
                    }
                }
            });
            // Inflate the layout for this fragment
            fs.collection("exhibition").whereEqualTo(FieldPath.documentId(), id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot document : task.getResult()){
                            if(document.exists()){
                                Exhibition tmp = document.toObject(Exhibition.class);
                                viewList[finalI].setOnClickListener(((FragmentsManager)getActivity()).exhibitDetailListener(tmp));
                            }
                        }
                    }
                }
            });
            Log.i("TAG", "onCreateView: getParentFragmentManager().toString()");
        }
        return rootView;
    }
}