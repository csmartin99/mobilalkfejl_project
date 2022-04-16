package com.example.csm_lakstextil_webshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF = MainActivity.class.getPackage().toString();
    private static final int RESULT_SIGN_IN = 111;
    private static final int SECRET_KEY = 57;

    private SharedPreferences pref;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    EditText userNameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameET = findViewById(R.id.editTextUserName);
        passwordET = findViewById(R.id.editTextPassword);

        pref = getSharedPreferences(PREF, MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id_copy)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

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
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", userNameET.getText().toString());
        editor.putString("password", passwordET.getText().toString());
        editor.apply();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);

        if(requestCode == RESULT_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG, "Logged in as: " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(LOG_TAG, "Login failed.", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "Logged in with Google Account");
                    goToProducts();
                } else {
                    Log.d(LOG_TAG, "Login failed.");
                    Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(View view) {
        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();

        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "Logged in as: " + userName);
                    goToProducts();
                } else {
                    Log.d(LOG_TAG, "Login failed.");
                    Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register(View view) {
        Intent regIntent = new Intent(this, RegActivity.class);
        regIntent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(regIntent);
    }

    public void loginAsGuest(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "Logged in as guest");
                    goToProducts();
                } else {
                    Log.d(LOG_TAG, "Login failed.");
                    Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginWithGoogle(View view) {
        Intent signInI = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInI, RESULT_SIGN_IN);
    }

    private void goToProducts() {
        Intent prodI = new Intent(this, ProductsActivity.class);
        //prodI.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(prodI);
    }
}