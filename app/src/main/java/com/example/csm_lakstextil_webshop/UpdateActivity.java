package com.example.csm_lakstextil_webshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    private static final String LOG_TAG = UpdateActivity.class.getName();
    private Context mContext;
    String productId;
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
        setContentView(R.layout.activity_update);

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
        productId = (String) getIntent().getSerializableExtra("PRODUCT");
    }

    public void updateData(View view) {
        String productImg = productImgET.getText().toString();
        String productName = productNameET.getText().toString();
        String productDesc = productDescET.getText().toString();
        String productPrice = productPriceET.getText().toString();

        DocumentReference reference = mProducts.document(productId);
        reference.update("productImg", 2131230809, "productName", productName, "productDesc", productDesc, "productPrice", productPrice).addOnSuccessListener(success -> {
            Toast.makeText(this, "Product updated: " + productId, Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "Product updated: " + productId);
        }).addOnFailureListener(failure -> {
            Toast.makeText(this, "Can't updated product: " + productId, Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "Can't updated product: " + productId);
        });
        //Intent productsIntent = new Intent(this, ProductsActivity.class);
        //startActivity(productsIntent);
    }

    public void back(View view) {
        finish();
    }
}
