package com.example.csm_lakstextil_webshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.widget.SearchView;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProductsActivity.class.getName();
    private static final int SECRET_KEY = 57;
    private FirebaseUser account;

    private ArrayList<Product> mProductList;
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private int gridNum = 1;
    private int cartI = 0;
    private boolean viewStyle = true;
    private FrameLayout notiCircle;
    private TextView contentTextView;
    private FirebaseFirestore mFirestore;
    private CollectionReference mProducts;
    private int productsQueryLimit = 6;

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

        mFirestore = FirebaseFirestore.getInstance();
        mProducts = mFirestore.collection("products");
        QueryData();

        IntentFilter fltr = new IntentFilter();
        fltr.addAction(Intent.ACTION_BATTERY_OKAY);
        fltr.addAction(Intent.ACTION_BATTERY_LOW);
        this.registerReceiver(batteryReceiver, fltr);
    }

    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null)
                return;
            if (action == Intent.ACTION_BATTERY_OKAY || action == Intent.ACTION_SCREEN_ON) {
                productsQueryLimit = 6;
            }
            if (action == Intent.ACTION_BATTERY_LOW || action == Intent.ACTION_SCREEN_OFF) {
                productsQueryLimit = 2;
            }
            QueryData();
        }
    };

    private void QueryData() {
        mProductList.clear();
        mProducts.orderBy("productRating", Query.Direction.DESCENDING).limit(productsQueryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Product product = doc.toObject(Product.class);
                product.setproductId(doc.getId());
                mProductList.add(product);
            }
            if (mProductList.size() == 0) {
                InitData();
                QueryData();
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    public void UpdateData(Product product) {
        Intent updateIntent = new Intent(this, UpdateActivity.class);
        updateIntent.putExtra("PRODUCT", product._getproductId());
        startActivity(updateIntent);
        //finish();
    }

    public void DeleteData(Product product) {
        DocumentReference reference = mProducts.document(product._getproductId());
        reference.delete().addOnSuccessListener(success -> {
            Log.d(LOG_TAG, "Product deleted: " + product._getproductId());
        }).addOnFailureListener(failure -> {
            Toast.makeText(this, "Can't delete product: " + product._getproductId(), Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "Can't delete product: " + product._getproductId());
        });
        QueryData();
    }

    private void InitData() {
        TypedArray productImg = getResources().obtainTypedArray(R.array.product_images);
        String[] productList = getResources().getStringArray(R.array.product_names);
        TypedArray productRating = getResources().obtainTypedArray(R.array.product_ratings);
        String[] productDesc = getResources().getStringArray(R.array.product_descs);
        String[] productPrice = getResources().getStringArray(R.array.product_prices);

        for (int i=0; i<productList.length; i++) {
            mProducts.add(new Product(productImg.getResourceId(i, 0), productList[i], productRating.getFloat(i, 0), productDesc[i], productPrice[i], 0));
        }

        productImg.recycle();
    }

    private void MostCartedProducts() {
        mProductList.clear();
        mProducts.whereGreaterThan("productCart", 5).orderBy("productCart", Query.Direction.DESCENDING).limit(5).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Product product = doc.toObject(Product.class);
                product.setproductId(doc.getId());
                mProductList.add(product);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    private void WorstRatedProducts() {
        mProductList.clear();
        mProducts.whereLessThan("productRating", 3).orderBy("productRating").limit(3).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Product product = doc.toObject(Product.class);
                product.setproductId(doc.getId());
                mProductList.add(product);
            }
            mAdapter.notifyDataSetChanged();
        });
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
            case R.id.add:
                Intent addIntent = new Intent(this, AddActivity.class);
                startActivity(addIntent);
                return true;
            case R.id.mostcartedproducts:
                MostCartedProducts();
                return true;
            case R.id.worstrated:
                WorstRatedProducts();
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

    public void UpdateIcon(Product product){
        cartI = cartI+1;
        if(cartI > 0){
            contentTextView.setText(String.valueOf(cartI));
        } else {
            contentTextView.setText("");
        }
        notiCircle.setVisibility(View.VISIBLE);
        mProducts.document(product._getproductId()).update("productCart", product.getProductCart() + 1).addOnFailureListener(faliure -> {
            Toast.makeText(this, "Can't modify product: " + product._getproductId(), Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "Can't modify product: " + product._getproductId());
        });
        QueryData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }

    protected void onStart() {
        super.onStart();
        QueryData();
    }
}