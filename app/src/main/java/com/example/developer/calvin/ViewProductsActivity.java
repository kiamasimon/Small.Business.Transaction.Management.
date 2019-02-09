package com.example.developer.calvin;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.developer.calvin.Adapters.HomeAdapter;
import com.example.developer.calvin.Adapters.ProductAdapter;
import com.example.developer.calvin.Models.Home;
import com.example.developer.calvin.Models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.GET_PRODUCTS;
import static com.example.developer.calvin.Constants.Url.GET_TRANSACTIONS;

public class ViewProductsActivity extends AppCompatActivity {

    ProductAdapter adapter;
    List<Product> productList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productList = new ArrayList<>();

        loadStock();
    }

    private void loadStock(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                productList.add(new Product(
                                        jsonObject.getInt("product_id"),
                                        jsonObject.getString("product_name"),
                                        jsonObject.getString("received_date"),
                                        jsonObject.getString("expiry_date"),
                                        jsonObject.getString("number_of_items"),
                                        jsonObject.getString("price_per_commodity"),
                                        jsonObject.getString("selling_price")

                                ));
                            }
                            adapter = new ProductAdapter(getApplicationContext(),productList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sharedPreferences.getString("user_id",""));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}
