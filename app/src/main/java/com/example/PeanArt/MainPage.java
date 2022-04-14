package com.example.PeanArt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    ExhibitionDetailFragment exhibitionDetailFragment = new ExhibitionDetailFragment();
    MyPageFragment myPageFragment = new MyPageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.underbar);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_layout, exhibitionDetailFragment).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeItem:
                        break;
                    case R.id.likedItem:
                        break;
                    case R.id.recommendItem:
                        break;
                    case R.id.profileItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_layout, myPageFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
    }

}