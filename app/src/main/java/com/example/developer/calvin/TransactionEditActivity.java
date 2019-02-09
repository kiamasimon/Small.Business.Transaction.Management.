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
import com.example.developer.calvin.Adapters.HomeAdapter;
import com.example.developer.calvin.Fragments.HomeFragment;
import com.example.developer.calvin.Models.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.DELETE_TRANSACTION;
import static com.example.developer.calvin.Constants.Url.GET_PURCHASES;
import static com.example.developer.calvin.Constants.Url.GET_TRANSACTION_DETAILS;
import static com.example.developer.calvin.Constants.Url.UPDATE_TRANSACTION_;

public class TransactionEditActivity extends AppCompatActivity {
    EditText edProductName, edReceivedDate, edExpiryDate, edQuantity,edPricePerCommodity,edSellingPrice;
    Button bnUpdate,bnDelete;
    SharedPreferences sharedPreferences;
    String name,expiry_date,received_date,selling_price,quantity,price_per_commodity,TransactionId;
    Bundle bundle;
    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        edProductName = findViewById(R.id.edProductName);
        edReceivedDate = findViewById(R.id.edReceivedDate);
        edExpiryDate = findViewById(R.id.edExpiryDate);
        edQuantity = findViewById(R.id.edQuantity);
        edPricePerCommodity = findViewById(R.id.edPricePerCommodity);
        edSellingPrice = findViewById(R.id.edSellingPrice);
        bnDelete = findViewById(R.id.bnDelete);
        bnUpdate = findViewById(R.id.bnUpdate);

        bundle = getIntent().getExtras();
        TransactionId = String.valueOf(bundle.getInt("purchase_id"));
        Log.i("conn",""+TransactionId);

        loadTransactionDetails();
        bnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTransaction();
            }
        });

        bnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTransaction();
            }
        });
    }

    private void loadTransactionDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TRANSACTION_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn", "" + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                 name = jsonObject.getString("product_name");
                                 received_date = jsonObject.getString("received_date");
                                 expiry_date = jsonObject.getString("expiry_date");
                                 quantity = jsonObject.getString("number_of_items");
                                 price_per_commodity = jsonObject.getString("price_per_commodity");
                                 selling_price = jsonObject.getString("selling_price");
                            }
                            edProductName.setText(name);
                            edReceivedDate.setText(received_date);
                            edExpiryDate.setText(expiry_date);
                            edQuantity.setText(quantity);
                            edPricePerCommodity.setText(price_per_commodity);
                            edSellingPrice.setText(selling_price);
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
                params.put("product_id", TransactionId);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    public void updateTransaction(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_TRANSACTION_,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        if (response.equals("Transaction successfully updated")){
                            showSnackbar(response);
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
                params.put("product_id", TransactionId);
                params.put("product_name", edProductName.getText().toString().trim());
                params.put("received_date", edReceivedDate.getText().toString().trim());
                params.put("expiry_date", edExpiryDate.getText().toString().trim());
                params.put("user_id", sharedPreferences.getString("user_id",""));
                params.put("number_of_items", edQuantity.getText().toString().trim());
                params.put("price_per_commodity", edPricePerCommodity.getText().toString().trim());
                params.put("selling_price", edSellingPrice.getText().toString().trim());
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public void deleteTransaction(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_TRANSACTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        if (response.equals("Transaction successfully deleted.")){
                            Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
                            startActivity(intent);
                            edProductName.setText("");
                            edExpiryDate.setText("");
                            edReceivedDate.setText("");
                            edQuantity.setText("");
                            edPricePerCommodity.setText("");
                            edSellingPrice.setText("");
                            showSnackbar(response);

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
                params.put("product_id", TransactionId);
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
