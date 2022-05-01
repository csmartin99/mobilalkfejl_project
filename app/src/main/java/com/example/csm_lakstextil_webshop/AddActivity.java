package com.example.csm_lakstextil_webshop;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddActivity.class.getName();
    EditText productImgET;
    EditText productNameET;
    EditText productDescET;
    EditText productPriceET;
    private FirebaseFirestore mFirestore;
    private CollectionReference mProducts;
    private FirebaseUser account;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        account = FirebaseAuth.getInstance().getCurrentUser();
        if(account != null)
        {
            Log.d(LOG_TAG, "Successfull auth process.");
        } else {
            Log.d(LOG_TAG, "Failed auth process.");
            finish();
        }

        productImgET = findViewById(R.id.productImageEditText);
        productNameET = findViewById(R.id.productNameEditText);
        productDescET = findViewById(R.id.productDescEditText);
        productPriceET = findViewById(R.id.productPriceEditText);
        mFirestore = FirebaseFirestore.getInstance();
        mProducts = mFirestore.collection("products");
    }

    public void CreateData(View view) {
        String productImg = productImgET.getText().toString();
        String productName = productNameET.getText().toString();
        String productDesc = productDescET.getText().toString();
        String productPrice = productPriceET.getText().toString();

        mProducts.add(new Product(2131230809, productName, 0, productDesc, productPrice, 0)).addOnSuccessListener(success -> {
            Toast.makeText(this, "Product added", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "Product added");
        }).addOnFailureListener(failure -> {
            Toast.makeText(this, "Product add failed", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "Product add failed");
        });
    }

    public void back(View view) {
        finish();
    }
}
