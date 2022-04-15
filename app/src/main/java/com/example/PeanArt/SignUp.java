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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    // Member Variable
    Button signBTN;
    EditText usernameETXT, nicknameETXT, pwdETXT, emailETXT;
    private FirebaseAuth mAuth;
    private String TAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signBTN = (Button) findViewById(R.id.signBTN);
        usernameETXT = (EditText) findViewById(R.id.usernameETXT);
        nicknameETXT = (EditText) findViewById(R.id.nicknameETXT);
        pwdETXT = (EditText) findViewById(R.id.pwdETXT);
        emailETXT = (EditText) findViewById(R.id.emailETXT);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){ //sangho
            //pass
        }else{
            Log.i(TAG, "이미 로그인 했습니다");
        }
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
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(SignUp.this, "Authentication success !!",
                                    Toast.LENGTH_SHORT).show();

                            sign.putExtra("userinfo", user);
                            setResult(RESULT_OK, sign);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_CANCELED, sign);
                            finish();
                            updateUI(null);
                        }
                    }
                });

    }
    private void updateUI(FirebaseUser user) {
        //pass
    }
}