package com.example.developer.calvin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.developer.calvin.Models.Product;
import com.example.developer.calvin.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.ADD_TO_CART;

/**
 * Created by Developer on 1/24/2019.
 */

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ProductViewHolder> {
    private Context mCtx;
    private List<Product> productList;
    String Title,Description,Price;
    String Id;
    public SalesAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product product = productList.get(position);

        holder.id = product.getProductid();
        Log.i("conn",""+holder.id);
        holder.tvTitle.setText(product.getProduct_name());
        holder.tvPrice.setText(product.getSellingprice());
        holder.tvDescription.setText(product.getExpiry_date());
        holder.etPurchaseD.setText(product.getReceived_date());
        holder.items.setText(product.getNumber_of_items());

        holder.tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddtoCart(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int id;
        ImageView ivImage;
        TextView tvTitle, tvDescription, tvPrice, tvAddToCart,etPurchaseD,items;

        public ProductViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvTitle = (TextView) itemView.findViewById(R.id.tvProductTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvProductDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
            tvAddToCart = (TextView) itemView.findViewById(R.id.tvAddToCart);
            etPurchaseD = itemView.findViewById(R.id.etPurchaseD);
            items = itemView.findViewById(R.id.items);

        }
        @Override
        public void onClick(View view) {

        }
    }

    private void AddtoCart(final Product product){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_TO_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        if (response.equals("Item Added To Cart")){
                            Toast.makeText(mCtx,"Item Added To Cart",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.i("conn",""+ product.getProductid());
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences;
                sharedPreferences = mCtx.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                params.put("user_id", sharedPreferences.getString("user_id",""));
                params.put("product_id", String.valueOf(product.getProductid()));
                return params;
            }
        };
        Volley.newRequestQueue(mCtx).add(stringRequest);
    }

}