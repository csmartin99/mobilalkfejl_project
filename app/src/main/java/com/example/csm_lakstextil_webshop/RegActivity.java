package com.example.csm_lakstextil_webshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegActivity.class.getName();
    private static final String PREF = RegActivity.class.getPackage().toString();

    private SharedPreferences pref;

    EditText userNameET;
    EditText passwordET;
    EditText passwordReET;
    EditText emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // Bundle bundle = getIntent().getExtras();
        // int secretKey = bundle.getInt("SECRET_KEY");
        int secretKey = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secretKey != 57) {
            finish();
        }

        userNameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordReET = findViewById(R.id.passwordConfirmEditText);
        emailET = findViewById(R.id.emailEditText);

        pref = getSharedPreferences(PREF, MODE_PRIVATE);
        String userName = pref.getString("username", "");
        String password = pref.getString("password", "");

        userNameET.setText(userName);
        passwordET.setText(password);

        Log.i(LOG_TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    public void register(View view) {
        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordRe = passwordReET.getText().toString();
        String email = emailET.getText().toString();

        if (!password.equals(passwordRe)) {
            Log.e(LOG_TAG, "Passwords do NOT match!");
        } else {
            Log.i(LOG_TAG, "Registered as: " + userName + ", pw: " + password + ", email: " + email);
        }
    }

    public void back(View view) {
        finish();
    }
}