package com.example.developer.calvin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.DELETE_PRODUCT;
import static com.example.developer.calvin.Constants.Url.DELETE_TRANSACTION;
import static com.example.developer.calvin.Constants.Url.GET_TRANSACTION_DETAILS;
import static com.example.developer.calvin.Constants.Url.PRODUCT_DETAILS;
import static com.example.developer.calvin.Constants.Url.UPDATE_PRODUCT;
import static com.example.developer.calvin.Constants.Url.UPDATE_TRANSACTION_;

public class ProductEditActivity extends AppCompatActivity {
    Bundle bundle;
    String product;
    EditText edProductName,edReceivedDate,edExpiryDate,edNumberOfItems,edPricePerCommodity;
    Button bnUpdate,bnDelete;
    String name,received,expiry,items,price;
    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        edProductName = findViewById(R.id.edProductName);
        edReceivedDate = findViewById(R.id.edDateReceived);
        edExpiryDate = findViewById(R.id.edExpiryDate);
        edNumberOfItems = findViewById(R.id.edNumberOfItems);
        edPricePerCommodity = findViewById(R.id.edPricePerCommodity);
        bnUpdate = findViewById(R.id.bnUpdate);
        bnDelete = findViewById(R.id.bnDelete);

        bundle = getIntent().getExtras();
        product = String.valueOf(bundle.getInt("product_id"));
        loadProduct();

        bnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
            }
        });

        bnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });
    }

    private void loadProduct() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PRODUCT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn", "" + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                name = jsonObject.getString("product_name");
                                received = jsonObject.getString("received_date");
                                expiry = jsonObject.getString("expiry_date");
                                items = jsonObject.getString("number_of_items");
                                price = jsonObject.getString("price_per_commodity");
                            }
                            edProductName.setText(name);
                            edReceivedDate.setText(received);
                            edExpiryDate.setText(expiry);
                            edNumberOfItems.setText(items);
                            edPricePerCommodity.setText(price);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    public void updateProduct(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        if (response.equals("Product successfully updated")){
                            showSnackbar(response);
                            edProductName.setText("");
                            edReceivedDate.setText("");
                            edExpiryDate.setText("");
                            edNumberOfItems.setText("");
                            edPricePerCommodity.setText("");
                            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                            startActivity(intent);
                        }else{
                            showSnackbar(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product);
                params.put("product_name", edProductName.getText().toString().trim());
                params.put("received_date", edReceivedDate.getText().toString().trim());
                params.put("expiry_date", edExpiryDate.getText().toString().trim());
                params.put("user_id", sharedPreferences.getString("user_id",""));
                params.put("number_of_items", edNumberOfItems.getText().toString().trim());
                params.put("price_per_commodity", edPricePerCommodity.getText().toString().trim());
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public void deleteProduct(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        if (response.equals("Product successfully deleted.")){
                            edProductName.setText("");
                            edReceivedDate.setText("");
                            edExpiryDate.setText("");
                            edNumberOfItems.setText("");
                            edPricePerCommodity.setText("");
                            showSnackbar(response);
                            Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
                            startActivity(intent);
                        }else{
                            showSnackbar(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product);
                Log.i("conn",""+product);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public void showSnackbar(String stringSnackbar){
        snackbar.make(findViewById(android.R.id.content), stringSnackbar.toString(), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }
}
