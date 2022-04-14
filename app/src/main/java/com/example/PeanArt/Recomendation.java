package com.example.PeanArt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Recomendation extends Fragment {

    // Member Variable

//    ImageView venusIMG, screamIMG, starryIMG, monarisaIMG, pearlIMG, kissIMG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.recomendation, container, false);

//        venusIMG = (ImageView)
//        screamIMG = (ImageView) findViewById(R.id.screamIMG);
//        starryIMG = (ImageView) findViewById(R.id.starryIMG);
//        monarisaIMG = (ImageView) findViewById(R.id.monarisaIMG);
//        pearlIMG = (ImageView) findViewById(R.id.pearlIMG);
//        kissIMG = (ImageView) findViewById(R.id.kissIMG);

        return rootView;
    }

//    public void rcmd(View view) {
//        switch (view.getId()){
//            case R.id.venusIMG:
//                Intent go_reco = new Intent(this, AiResult.class);
//                startActivity(go_reco);
//        }
//    }
}