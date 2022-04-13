package com.example.PeanArt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {
    // Member Variable
    Button signBTN;
    EditText usernameETXT, nicknameETXT, pwdETXT, emailETXT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signBTN = (Button) findViewById(R.id.signBTN);
        usernameETXT = (EditText) findViewById(R.id.usernameETXT);
        nicknameETXT = (EditText) findViewById(R.id.nicknameETXT);
        pwdETXT = (EditText) findViewById(R.id.pwdETXT);
        emailETXT = (EditText) findViewById(R.id.emailETXT);

    }

    public void sign(View view) {
        Intent sign = new Intent(this, Login.class);
        String user = usernameETXT.getText().toString();
        String nick = usernameETXT.getText().toString();
        String pwd = usernameETXT.getText().toString();
        String email = usernameETXT.getText().toString();

        sign.putExtra("user", user);
        sign.putExtra("nick", nick);
        sign.putExtra("pwd", pwd);
        sign.putExtra("email", email);

        setResult(RESULT_OK, sign);
        finish();
    }
}