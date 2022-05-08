package com.example.csm_lakstextil_webshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

//Firebase autentikáció meg van valósítva: Be lehet jelentkezni és regisztrálni
//Legalább 3 különböző activity használata
public class RegActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String LOG_TAG = RegActivity.class.getName();
    private static final String PREF = RegActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 57;

    private SharedPreferences pref;
    private FirebaseAuth mAuth;
    private CollectionReference mAccount;
    private FirebaseFirestore mFirestore;
    FirebaseUser user;

    EditText userNameET;
    EditText passwordET;
    EditText passwordReET;
    EditText emailET;
    EditText phoneET;
    EditText postaladdressET;

    Spinner phoneSpinner;

    RadioGroup userRG;

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
        passwordET = findViewById(R.id.productImageEditText);
        passwordReET = findViewById(R.id.productDescEditText);
        emailET = findViewById(R.id.productPriceEditText);
        phoneET = findViewById(R.id.phoneEditText);
        postaladdressET = findViewById(R.id.addressEditText);

        phoneSpinner = findViewById(R.id.phoneNumberSpinner);

        userRG = findViewById(R.id.userRadioGroup);
        userRG.check(R.id.userRadioButton);

        pref = getSharedPreferences(PREF, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        String userName = pref.getString("username", "");
        String password = pref.getString("password", "");

        userNameET.setText(userName);
        passwordET.setText(password);

        phoneSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> phoneAdapter = ArrayAdapter.createFromResource(this, R.array.phoneNumber, android.R.layout.simple_spinner_item);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneSpinner.setAdapter(phoneAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mAccount = mFirestore.collection("user");
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
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
        String phone = phoneET.getText().toString();
        String phoneType = phoneSpinner.getSelectedItem().toString();
        String postalAddress = postaladdressET.getText().toString();
        int userChecked = userRG.getCheckedRadioButtonId();
        RadioButton userRadioBtn = userRG.findViewById(userChecked);
        String userType = userRadioBtn.getText().toString();

        if(password.length()<6) {
            Log.e(LOG_TAG, "Password is too short! (Min 6)");
            Toast.makeText(RegActivity.this, "Password is too short! (Min 6)", Toast.LENGTH_SHORT).show();
        }
        if (!password.equals(passwordRe)) {
            Log.e(LOG_TAG, "Passwords do NOT match!");
            Toast.makeText(RegActivity.this, "Passwords do NOT match!", Toast.LENGTH_SHORT).show();
        } else {
            //Log.i(LOG_TAG, "Registered as: " + userName + ", pw: " + password + ", email: " + email);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        user = mAuth.getCurrentUser();
                        goToProducts();
                        finish();
                    }
                    else{
                        Log.d(LOG_TAG, "Account failed to create.");
                        Toast.makeText(RegActivity.this, "Account failed to create: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void goToProducts() {
        Intent prodI = new Intent(this, ProductsActivity.class);
        //prodI.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(prodI);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selected = adapterView.getItemAtPosition(i).toString();
        Log.i(LOG_TAG, selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}