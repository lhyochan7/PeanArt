package com.example.PeanArt;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentsManager extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainPage fragmentMain = new MainPage();
    private Recomendation fragmentRecommend = new Recomendation();
    private LikeList fragmentLike = new LikeList();
    private MyPage fragmentMypage = new MyPage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainLAY, fragmentMain).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.homeItem:
                    transaction.replace(R.id.mainLAY, fragmentMain).commitAllowingStateLoss();
                    break;
                case R.id.recommendItem:
                    transaction.replace(R.id.mainLAY, fragmentRecommend).commitAllowingStateLoss();
                    break;
                case R.id.likedItem:
                    transaction.replace(R.id.mainLAY, fragmentLike).commitAllowingStateLoss();
                    break;
                case R.id.myPageItem:
                    transaction.replace(R.id.mainLAY, fragmentMypage).commitAllowingStateLoss();
                    break;
            }

            return true;
        }
    }
}