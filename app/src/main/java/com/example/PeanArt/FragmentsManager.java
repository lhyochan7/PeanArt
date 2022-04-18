package com.example.PeanArt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class FragmentsManager<var> extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainPage fragmentMain = new MainPage();
    private Recomendation fragmentRecommend = new Recomendation();
    private LikeList fragmentLike = new LikeList();
    private MyPage fragmentMypage = new MyPage();

    String uid = null;

    private final String TAG = "FragManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
        }


        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        fragmentMain.setArguments(bundle);
        fragmentRecommend.setArguments(bundle);
        fragmentLike.setArguments(bundle);
        fragmentMypage.setArguments(bundle);
        Log.i(TAG, "UID : " + uid);
        fragmentManager.beginTransaction()
                .replace(R.id.mainLAY, fragmentMain)
                .commit();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.homeItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLAY, fragmentMain).commit();
                    break;
                case R.id.recommendItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLAY, fragmentRecommend).commit();
                    break;
                case R.id.likedItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLAY, fragmentLike).commit();
                    break;
                case R.id.myPageItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLAY, fragmentMypage).commit();
                    break;
            }

            return true;
        }
    }
}