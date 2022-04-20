package com.example.PeanArt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterGallery extends AppCompatActivity {
    //DEGUG
    private final static String TAG="RegisterGallery";

    //view
    private EditText edit_exhibition_name; //전시회 이름
    private EditText edit_exhibition_detail; //상세 설명
    private EditText edit_exhibition_info; //간략 정보
    private EditText edit_location;//전시 장소(미구현)
    private EditText edit_exhibition_uri; //전시 uri
    private ImageView input_image; //전시 이미지
    private Button btn_register; //등록 버튼
    private Spinner spinner_category;
    private Spinner spinner_kind;

    //firebase
    private FirebaseStorage storage; // 스토리지 접근하기 위한 인스턴스
    private FirebaseFirestore db;
    private Uri img_data;

    //const
    private final int GALLERY_CODE = 10; //갤러리 접근하기 위한 코드
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    //date view variable
    private View view_focus;
    private String start_date;
    private String end_date;
    private int cnt = 1;

    //test variable
    private String test_uid ="XW0VDeicIQWrVwYZAwPRPTXaySc2"; //MainPage?? 에서 userinfo 던져주세요

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gallery);

        //view finder
        input_image = findViewById(R.id.input_image);
        btn_register = findViewById(R.id.btn_register);

        //firebase instance init
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        input_image.setOnClickListener(new View.OnClickListener() { //imageView : album load click event
            @Override
            public void onClick(View view){
                loadAlbum();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() { // buttonView : register exhibition click event
            @Override
            public void onClick(View view) {
                Exhibition_add_func(test_uid);
            }
        });

        // Spinner
        Spinner yearSpinner = (Spinner)findViewById(R.id.spinner_year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.kind_list, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        Spinner monthSpinner = (Spinner)findViewById(R.id.spinner_month);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.category_list, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        CollectionReference ad = db.collection("exhibition");
        ad.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        cnt++;
                    }
                }
            }
        });

    }
    public void Exhibition_add_func(String uid){
        edit_exhibition_name =  findViewById(R.id.edit_exhibition_name);
        edit_exhibition_detail =  findViewById(R.id.edit_exhibition_detail);
        edit_exhibition_info =  findViewById(R.id.edit_exhibition_info);
        edit_location =  findViewById(R.id.edit_location);
        edit_exhibition_uri =  findViewById(R.id.edit_exhibition_uri);
        spinner_category = findViewById(R.id.spinner_month);
        spinner_kind = findViewById(R.id.spinner_year);

        Map<String, Object> exhibition_input_string = new HashMap<>();

        exhibition_input_string.put("UID", uid);
        exhibition_input_string.put("URI", edit_exhibition_uri.getText().toString());
        exhibition_input_string.put("category", spinner_category.getSelectedItem());
        exhibition_input_string.put("detail", edit_exhibition_detail.getText().toString());
        exhibition_input_string.put("enddate", end_date);
        exhibition_input_string.put("info", edit_exhibition_info.getText().toString());
        exhibition_input_string.put("kind", spinner_kind.getSelectedItem());
        exhibition_input_string.put("location", "41°24'12.2\"N 2°10'26.5\"E");
        exhibition_input_string.put("startdate",start_date);
        exhibition_input_string.put("title", edit_exhibition_name.getText().toString());


        db.collection("exhibition").document("gallery"+cnt)
                .set(exhibition_input_string)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        image_upload_to_storage("gallery"+cnt); //fireStorage img update func
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void loadAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,GALLERY_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE){
            try{
                img_data = data.getData();
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                input_image.setImageBitmap(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void image_upload_to_storage(String path){ //사진 firestorage upload function
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("Exhibition/"+path+"/"+"1");
        UploadTask uploadTask = riversRef.putFile(img_data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG,"image fail");
                Toast.makeText(RegisterGallery.this, "사진이 " +
                        "업로드 안됨.", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG,"image success");
                Toast.makeText(RegisterGallery.this, "사진이 정상적으로 업로드 " +
                        "되엇습니다.",Toast.LENGTH_LONG).show();
            }
        });
    }

    //date picker func
    public void showDatePicker(View view) {  //click event
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
        view_focus = view;
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string +"/" + month_string + "/" + day_string);
        Log.i(TAG, dateMessage);
        TextView start_txt = findViewById(R.id.txt_start_day);
        TextView end_txt = findViewById(R.id.txt_end_day);
        if(view_focus == start_txt){
            start_date =dateMessage;
            start_txt.setText(start_date);
        }else{
            end_date =dateMessage;
            end_txt.setText(end_date);
        }
    }
}

