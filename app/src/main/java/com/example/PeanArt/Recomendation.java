package com.example.PeanArt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Recomendation extends AppCompatActivity {

    // Member Variable

    ImageView venusIMG, screamIMG, starryIMG, monarisaIMG, pearlIMG, kissIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recomendation);

        venusIMG = (ImageView) findViewById(R.id.venusIMG);
        screamIMG = (ImageView) findViewById(R.id.screamIMG);
        starryIMG = (ImageView) findViewById(R.id.starryIMG);
        monarisaIMG = (ImageView) findViewById(R.id.monarisaIMG);
        pearlIMG = (ImageView) findViewById(R.id.pearlIMG);
        kissIMG = (ImageView) findViewById(R.id.kissIMG);

    }

    public void rcmd(View view) {
        switch (view.getId()){
            case R.id.venusIMG:
                Intent go_reco = new Intent(this, AiResult.class);
                startActivity(go_reco);
        }
    }
}