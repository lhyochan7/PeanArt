package com.example.PeanArt;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.PeanArt.model.Exhibition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

public class FragmentsManager<var> extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainPage fragmentMain = new MainPage();
    private Recommendation fragmentRecommend = new Recommendation();
    private LikeList fragmentLike = new LikeList();
    private MyPage fragmentMypage = new MyPage();
    private ExhibitionDetailFragment exhibitionDetailFragment = new ExhibitionDetailFragment();
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
    public View.OnClickListener exhibitDetailListener(Exhibition exhibition){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.cardView){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("exhibition", exhibition);
                    Log.i(TAG, exhibition.getId());
                    exhibitionDetailFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLAY, exhibitionDetailFragment).commit();
                }
            }
        };
    }
    public View.OnClickListener onLikeListener(String exhibitionID){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DocumentReference DocRef = LikeList.fs.collection("test_users").document(uid);
                LikeList.fs.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(DocRef);
                        ArrayList<String> tmpList = (ArrayList<String>) snapshot.get("liked");
                        if(tmpList.contains(exhibitionID)){
                            tmpList.remove(exhibitionID);
                        } else {
                            tmpList.add(exhibitionID);
                        }
                        transaction.update(DocRef, "liked", tmpList);
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "Transaction success!");
                        if((Boolean)view.getTag()){
                            ((ImageButton)view).setImageResource(R.drawable.unlikedicon);
                            view.setTag(false);
                        } else{
                            ((ImageButton)view).setImageResource(R.drawable.likedicon);
                            view.setTag(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Transaction failure.", e);
                    }
                });
            }
        };
    }
}