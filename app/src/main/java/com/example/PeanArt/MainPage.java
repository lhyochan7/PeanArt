package com.example.PeanArt;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Member;

public class MainPage extends AppCompatActivity {

    private String TAG = "MainPage";

    private Button registerBTN;

    // Feature
    private ActivityResultLauncher<Intent> mLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBTN = (Button) findViewById(R.id.registerBTN);


        // (3) 데이터 준비
        mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>(){
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()==RESULT_OK) {
                            // 결과 + 데이터가 온 경우 => Intent 객체를 꺼내기
                            Intent data = result.getData();
                            Toast.makeText(MainPage.this, "RECEIVE DATA : ", Toast.LENGTH_SHORT).show();

                        } else if(result.getResultCode()==RESULT_CANCELED) {
                            Log.i(TAG, "CANCELED");
                        }
                    }
                });
    }


    public void clickFunc(View view) {
        switch(view.getId()) {
            case R.id.registerBTN:
                mLauncher.launch(new Intent(this, RegisterGallery.class));
                break;
        }

    }

}