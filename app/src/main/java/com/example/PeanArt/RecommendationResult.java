package com.example.PeanArt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecommendationResult extends AppCompatActivity {

    private ImageView topThree1, topThree2, topThree3;

    private FirebaseStorage storage;
    private StorageReference storageRef;


    String topThree [];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_result);

        storage = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com/");
        storageRef = storage.getReference();

        topThree1 = (ImageView) findViewById(R.id.topThree1);
        topThree2 = (ImageView) findViewById(R.id.topThree2);
        topThree3 = (ImageView) findViewById(R.id.topThree3);

        topThree = new String[3];

        Intent topIntent = getIntent();
        topThree = topIntent.getStringArrayExtra("topThree");

        Log.i("MainActivity2", "MAIN 2 -> Recieved" + topThree[1]);

        for(int i=0; i<topThree.length; i++) {
            String path = topThree[i];
            int finalI = i;

            Log.i("MainActivity2", "TOP THREE ID = " + path);

            String arr [] = path.split("/");

            String id = arr[2];

            Log.i("MainActivigy2", "ID = " + id);

            storageRef.child(path).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        switch(finalI) {
                            case 0:
                                Glide.with(getApplicationContext()).load(task.getResult()).into(topThree1);
                                break;

                            case 1:
                                Glide.with(getApplicationContext()).load(task.getResult()).into(topThree2);
                                break;

                            case 2:
                                Glide.with(getApplicationContext()).load(task.getResult()).into(topThree3);
                                break;
                        }
                    }
                }
            });


        }
    }

}