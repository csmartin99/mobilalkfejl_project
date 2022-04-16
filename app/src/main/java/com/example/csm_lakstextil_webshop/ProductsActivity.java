package com.example.csm_lakstextil_webshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SearchView;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProductsActivity.class.getName();
    private FirebaseUser account;

    private ArrayList<Product> mProductList;
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private int gridNum = 1;
    private int cartI = 0;
    private boolean viewStyle = true;
    private FrameLayout notiCircle;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        account = FirebaseAuth.getInstance().getCurrentUser();
        if(account != null)
        {
            Log.d(LOG_TAG, "Successfull auth process.");
        } else {
            Log.d(LOG_TAG, "Failed auth process.");
            finish();
        }

        mProductList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rW);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNum));
        mAdapter = new ProductAdapter(this,mProductList);
        mRecyclerView.setAdapter(mAdapter);

        InitData();
    }

    private void InitData() {
        TypedArray productImg = getResources().obtainTypedArray(R.array.product_images);
        String[] productList = getResources().getStringArray(R.array.product_names);
        TypedArray productRating = getResources().obtainTypedArray(R.array.product_ratings);
        String[] productDesc = getResources().getStringArray(R.array.product_descs);
        String[] productPrice = getResources().getStringArray(R.array.product_prices);

        mProductList.clear();

        for (int i=0; i<productList.length; i++) {
            mProductList.add(new Product(productImg.getResourceId(i, 0), productList[i], productRating.getFloat(i, 0), productDesc[i], productPrice[i]));
        }

        productImg.recycle();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.products_menu, menu);
        MenuItem mI = menu.findItem(R.id.search);
        SearchView sW = (SearchView) MenuItemCompat.getActionView(mI);
        sW.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.i(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem product) {
        switch (product.getItemId()) {
            case R.id.cart:
                return true;
            case R.id.search:
                return true;
            case R.id.view:
                if (viewStyle) {
                    changeSpanCount(product, R.drawable.view, 1);
                } else {
                    changeSpanCount(product, R.drawable.view2, 2);
                }
                return true;
            case R.id.settings:
                return true;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(product);
        }
    }

    private void changeSpanCount(MenuItem product, int view2, int i) {
        viewStyle = !viewStyle;
        product.setIcon(view2);
        GridLayoutManager lM = (GridLayoutManager) mRecyclerView.getLayoutManager();
        lM.setSpanCount(i);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem aMI = menu.findItem(R.id.cart);
        FrameLayout rW = (FrameLayout) aMI.getActionView();
        notiCircle = (FrameLayout) rW.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rW.findViewById(R.id.view_alert_count_textview);
        rW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(aMI);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void UpdateIcon(){
        cartI = cartI+1;
        if(cartI > 0){
            contentTextView.setText(String.valueOf(cartI));
        } else {
            contentTextView.setText("");
        }
    }
}