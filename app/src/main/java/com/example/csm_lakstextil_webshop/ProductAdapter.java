package com.example.csm_lakstextil_webshop;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {
    private static final int SECRET_KEY = 57;
    private int lP = -1;
    private Context mContext;
    private ArrayList<Product> mProductListAll;
    private ArrayList<Product> mProductList;
    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Product> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mProductListAll.size();
                results.values = mProductListAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Product item : mProductListAll) {
                    if (item.getProductName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mProductList = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    public ProductAdapter(Context con, ArrayList<Product> productsList) {
        this.mContext = con;
        this.mProductListAll = productsList;
        this.mProductList = productsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.product_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product currentProduct = mProductList.get(position);
        holder.bindTo(currentProduct);

        if(holder.getAdapterPosition() > lP) {
            Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_r);

            holder.itemView.startAnimation(anim);
            lP = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mProdImage;
        private TextView mProdNameText;
        private RatingBar mProdRating;
        private TextView mProdDescText;
        private TextView mProdPriceText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mProdImage = itemView.findViewById(R.id.productImage);
            mProdNameText = itemView.findViewById(R.id.productName);;
            mProdRating = itemView.findViewById(R.id.productRatingBar);
            mProdDescText = itemView.findViewById(R.id.productDescription);
            mProdPriceText = itemView.findViewById(R.id.productPrice);
        }

        public void bindTo(Product currentProduct) {
            Glide.with(mContext).load(currentProduct.getProductImg()).into(mProdImage);
            mProdNameText.setText(currentProduct.getProductName());
            mProdRating.setRating(currentProduct.getProductRating());
            mProdDescText.setText(currentProduct.getProductDesc());
            mProdPriceText.setText(currentProduct.getProductPrice());

            itemView.findViewById(R.id.productAddToCart).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //Legal??bb 2 k??l??nb??z?? anim??ci?? haszn??lata
                    Log.d("A", "Added to cart.");
                    ((ProductsActivity)mContext).UpdateIcon(currentProduct);
                    Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.btnpress);
                    itemView.findViewById(R.id.productAddToCart).startAnimation(anim);
                }
            });

            itemView.findViewById(R.id.productDelete).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.d("A", "Deleted.");
                    ((ProductsActivity)mContext).DeleteData(currentProduct);
                }
            });

            itemView.findViewById(R.id.productUpdate).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.d("A", "Update.");
                    ((ProductsActivity)mContext).UpdateData(currentProduct);
                }
            });
            
        }
    }
}


