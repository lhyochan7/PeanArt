package com.example.PeanArt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    // view
    Button signBTN;
    EditText usernameETXT, pwdETXT, emailETXT;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //debug
    private String TAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //view
        signBTN = (Button) findViewById(R.id.signBTN);
        usernameETXT = (EditText) findViewById(R.id.usernameETXT);
        pwdETXT = (EditText) findViewById(R.id.pwdETXT);
        emailETXT = (EditText) findViewById(R.id.emailETXT);

        //firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void sign(View view) {
        Intent sign = new Intent(this, Login.class);

        String nick = usernameETXT.getText().toString();
        String pwd = pwdETXT.getText().toString();
        String email = emailETXT.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            sign.putExtra("userinfo", user);
                            user_add_func(user.getUid(), nick);
                            setResult(RESULT_OK, sign);

                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_CANCELED, sign);
                            finish();

                        }
                    }
                });
    }

    public void user_add_func(String uid, String nick){
        Log.i(TAG,uid+" / "+nick);
        Map<String, Object> user_input = new HashMap<>();
        user_input.put("NICK", nick);

        db.collection("test_users").document(uid)
                .set(user_input)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

}