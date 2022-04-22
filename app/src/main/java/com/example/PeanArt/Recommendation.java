package com.example.PeanArt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.PeanArt.ml.EncodedModel224;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Recommendation extends Fragment implements View.OnClickListener {

    private final String TAG = "Recommendation";

    // Member Variable
    String uid;

    // UI 선언
    private EncodedModel224 model;
    private ImageView imageButton1, imageButton2, imageButton3, imageButton4, imageButton5, imageButton6;
    private LinearLayout loadingScreen, recommendScreen;
    private TextView progressTV;

    // Firebase 선언
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference recommendImgs;
    private StorageReference imageChoices;


    // 사용 변수들 선언
    private Map<String, float[]> dataList;

    private Map<String, Float> dists;

    private ByteBuffer byteBuffer;
    private String path;
    private String topThree[];
    private int loop_cnt, imgCNT, idx, total_cnt;
    private Set<Integer> imgIds;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.recomendation, container, false);

        // UI 선언
        progressTV = (TextView) rootView.findViewById(R.id.progressTV);

        imageButton1 = (ImageView) rootView.findViewById(R.id.imageButton1);
        imageButton1.setDrawingCacheEnabled(true);
        imageButton2 = (ImageView) rootView.findViewById(R.id.imageButton2);
        imageButton2.setDrawingCacheEnabled(true);
        imageButton3 = (ImageView) rootView.findViewById(R.id.imageButton3);
        imageButton3.setDrawingCacheEnabled(true);
        imageButton4 = (ImageView) rootView.findViewById(R.id.imageButton4);
        imageButton4.setDrawingCacheEnabled(true);
        imageButton5 = (ImageView) rootView.findViewById(R.id.imageButton5);
        imageButton5.setDrawingCacheEnabled(true);
        imageButton6 = (ImageView) rootView.findViewById(R.id.imageButton6);
        imageButton6.setDrawingCacheEnabled(true);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);
        imageButton6.setOnClickListener(this);

        // Firesbase Storage 초기 선언
        storage = FirebaseStorage.getInstance("gs://peanart-b433a.appspot.com/");
        storageRef = storage.getReference();
        recommendImgs = storageRef.child("Exhibition/");
        imageChoices = storageRef.child("Recommendation_images/");


        // Loading Screen으로 초기화
        loadingScreen = (LinearLayout) rootView.findViewById(R.id.loadingScreen);
        recommendScreen = (LinearLayout) rootView.findViewById(R.id.recommendScreen);

        loadingScreen.setVisibility(View.VISIBLE);
        recommendScreen.setVisibility(View.GONE);

        // 변수 초기 선언
        dataList = new HashMap<String, float[]>();
        dists = new HashMap<String, Float>();
        byteBuffer = ByteBuffer.allocate(4 * 224 * 224 * 3);
        topThree = new String[3];
        loop_cnt = 0;
        total_cnt = 0;
        imgCNT = 1;
        imgIds = new LinkedHashSet<Integer>();


        findNumPicinDatabase();
        runModelonDatabase();
        chooseRecommendImages();

        return rootView;
    }

    public void findNumPicinDatabase() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference("/Exhibition");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.

                            prefix.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {
                                    // 해당 폴더 파일들 모두 찾기
                                    for (StorageReference item : listResult.getItems()) {
                                        total_cnt++;
                                    }
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    public void runModelonDatabase() {
        // DB에 있는 사진들 모델에 돌리기
        try {
            model = EncodedModel224.newInstance(getContext());
            StorageReference listRef = FirebaseStorage.getInstance().getReference("/Exhibition");
            listRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            progressTV.setText(loop_cnt + " / " + total_cnt);

                            Log.d(TAG, listRef.getName());

                            // 폴더 이름 찾기
                            for (StorageReference prefix : listResult.getPrefixes()) {
                                Log.d(TAG, prefix.getName());
                                // This will give you a folder name
                                // You may call listAll() recursively on them.

                                prefix.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                    @Override
                                    public void onSuccess(ListResult listResult) {

                                        // 해당 폴더 파일들 모두 찾기
                                        for (StorageReference item : listResult.getItems()) {
                                            item.getBytes(4096 * 4096)
                                                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                        @Override
                                                        public void onSuccess(byte[] bytes) {
                                                            //idx = Integer.parseInt(item.getName().replace(".jpg", ""));
                                                            path = item.getPath();

                                                            if (path.contains("poster")) {
                                                                // continue loop (skip)
                                                            } else {
                                                                Log.i(TAG, "PATH = " + path);

                                                                byteBuffer.clear();

                                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                                                Bitmap resizedImg = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

                                                                resizedImg.copyPixelsToBuffer(byteBuffer);

                                                                // Creates inputs for reference and load Buffer to bitmap
                                                                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                                                                inputFeature0.loadBuffer(byteBuffer);

                                                                // Runs model inference and gets result.
                                                                EncodedModel224.Outputs outputs = model.process(inputFeature0);
                                                                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                                                                // Get predicted feature values
                                                                float[] data = outputFeature0.getFloatArray();

                                                                normalize(data);

                                                                Log.i(TAG, "Data Value = " + data[100]);

                                                                dataList.put(path, data);

                                                                Log.i(TAG, "LOOP_CNT = " + loop_cnt);
                                                            }
                                                            loop_cnt++;

                                                            progressTV.setText(loop_cnt + " / " + total_cnt);

                                                            if ((loop_cnt >= total_cnt) && total_cnt != 0) {
                                                                loadingScreen.setVisibility(View.GONE);
                                                                recommendScreen.setVisibility(View.VISIBLE);
                                                            }

                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Uh-oh, an error occurred!
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseRecommendImages() {
        // Recommendation Image 넣기
        Random rand = new Random();

        while (imgIds.size() < 6) {
            imgIds.add(rand.nextInt((12 - 1) + 1) + 1);
        }

        imageChoices
                .listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {

                            item.getBytes(2048 * 2048)
                                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            idx = Integer.parseInt(item.getName().replace(".png", ""));
                                            Log.i(TAG, "IDX = " + idx);

                                            if (imgIds.contains(idx)) {
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                                Bitmap resizedImg = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                                                Log.i(TAG, "IMG ID = " + idx);

                                                switch (imgCNT++) {
                                                    case 1:
                                                        imageButton1.setImageBitmap(resizedImg);
                                                        break;
                                                    case 2:
                                                        imageButton2.setImageBitmap(resizedImg);
                                                        break;
                                                    case 3:
                                                        imageButton3.setImageBitmap(resizedImg);
                                                        break;
                                                    case 4:
                                                        imageButton4.setImageBitmap(resizedImg);
                                                        break;
                                                    case 5:
                                                        imageButton5.setImageBitmap(resizedImg);
                                                        break;
                                                    case 6:
                                                        imageButton6.setImageBitmap(resizedImg);
                                                        break;
                                                }

                                            }
                                        }
                                    });
                        }
                    }
                });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "RESUME UID : " + uid);
    }

    public static float getEuclideanDistance(float[] v1, float[] v2) {
        float sum = 0.0f;
        for (int i = 0; i < v1.length; i++) {
            //Log.i("MainActivity", String.format("v1 = %.5f", v1[i]));
            //Log.i("MainActivity", String.format("v2 = %.5f", v2[i]));
            sum += (v1[i] - v2[i]) * (v1[i] - v2[i]);
        }
        return (float) Math.sqrt(sum);
    }

    public static float[] normalize(float[] data) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        // find min & max
        for (float f : data) {
            if (f < min)
                min = f;
            if (f > max)
                max = f;
        }

        // update values
        for (int i = 0; i < data.length; i++) {
            float x = data[i];
            data[i] = (x - min) / (max - min);
        }

        return data;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        Bitmap image;
        Bitmap resizedImg;

        switch (view.getId()) {
            case R.id.imageButton1:
                byteBuffer.clear();

                imageButton1.destroyDrawingCache();   //destroy preview Created cache if any
                imageButton1.buildDrawingCache();

                // Convert image to bitmap and resize
                image = imageButton1.getDrawingCache();
                resizedImg = Bitmap.createScaledBitmap(image, 224, 224, true);
                resizedImg.copyPixelsToBuffer(byteBuffer);
                break;

            case R.id.imageButton2:
                byteBuffer.clear();

                imageButton2.destroyDrawingCache();   //destroy preview Created cache if any
                imageButton2.buildDrawingCache();

                // Convert image to bitmap and resize
                image = imageButton2.getDrawingCache();
                resizedImg = Bitmap.createScaledBitmap(image, 224, 224, true);
                resizedImg.copyPixelsToBuffer(byteBuffer);
                break;

            case R.id.imageButton3:
                byteBuffer.clear();

                imageButton3.destroyDrawingCache();   //destroy preview Created cache if any
                imageButton3.buildDrawingCache();

                // Convert image to bitmap and resize
                image = imageButton3.getDrawingCache();
                resizedImg = Bitmap.createScaledBitmap(image, 224, 224, true);
                resizedImg.copyPixelsToBuffer(byteBuffer);
                break;

            case R.id.imageButton4:
                byteBuffer.clear();

                imageButton4.destroyDrawingCache();   //destroy preview Created cache if any
                imageButton4.buildDrawingCache();

                // Convert image to bitmap and resize
                image = imageButton4.getDrawingCache();
                resizedImg = Bitmap.createScaledBitmap(image, 224, 224, true);
                resizedImg.copyPixelsToBuffer(byteBuffer);
                break;

            case R.id.imageButton5:
                byteBuffer.clear();

                imageButton5.destroyDrawingCache();   //destroy preview Created cache if any
                imageButton5.buildDrawingCache();

                // Convert image to bitmap and resize
                image = imageButton5.getDrawingCache();
                resizedImg = Bitmap.createScaledBitmap(image, 224, 224, true);
                resizedImg.copyPixelsToBuffer(byteBuffer);
                break;

            case R.id.imageButton6:
                byteBuffer.clear();

                imageButton6.destroyDrawingCache();   //destroy preview Created cache if any
                imageButton6.buildDrawingCache();

                // Convert image to bitmap and resize
                image = imageButton6.getDrawingCache();
                resizedImg = Bitmap.createScaledBitmap(image, 224, 224, true);
                resizedImg.copyPixelsToBuffer(byteBuffer);
                break;

        }

        //Creates inputs for reference and load Buffer to bitmap
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
        inputFeature0.loadBuffer(byteBuffer);

        // Runs model inference and gets result.
        EncodedModel224.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

        // Get predicted feature values
        float[] inputData = outputFeature0.getFloatArray();

        normalize(inputData);

        // get Eucledian Distance and top Three recommendation images
        dists = new HashMap<String, Float>();

        for (String key : dataList.keySet()) {
            dists.put(key, getEuclideanDistance(dataList.get(key), inputData));
        }

        List<Map.Entry<String, Float>> list = new ArrayList<>(dists.entrySet());
        list.sort(Map.Entry.comparingByValue());


        for (int i = 0; i < 3; i++) {
            Map.Entry<String, Float> a = list.get(i);
            topThree[i] = a.getKey();

            Log.i(TAG, "topThree[" + i + "] = " + a.getValue());
        }

        for (int i = 0; i < topThree.length; i++) {
            Log.i(TAG, "topThree[" + i + "] = " + topThree[i]);
        }

        Intent intent = new Intent(getActivity(), RecommendationResult.class);
        intent.putExtra("topThree", topThree);
        startActivity(intent);
    }
}