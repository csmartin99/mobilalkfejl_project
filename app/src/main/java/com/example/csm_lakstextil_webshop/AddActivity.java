package com.example.csm_lakstextil_webshop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

//Legalább 3 különböző activity használata
public class AddActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddActivity.class.getName();
    EditText productImgET;
    EditText productNameET;
    EditText productDescET;
    EditText productPriceET;
    private FirebaseFirestore mFirestore;
    private CollectionReference mProducts;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
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

    //CRUD műveletek mindegyike megvalósult és műveletek
    public void CreateData(View view) {
        String productImg = productImgET.getText().toString();
        String productName = productNameET.getText().toString();
        String productDesc = productDescET.getText().toString();
        String productPrice = productPriceET.getText().toString();
        int drawableResource = getResources().getIdentifier(productImg, "drawable", getPackageName());

        mProducts.add(new Product(drawableResource, productName, 0, productDesc, productPrice, 0)).addOnSuccessListener(success -> {
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
