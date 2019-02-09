package com.example.developer.calvin;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.developer.calvin.Adapters.HomeAdapter;
import com.example.developer.calvin.Models.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.GET_SEARCH;
import static com.example.developer.calvin.Constants.Url.GET_TRANSACTIONS;

public class SearchResultsActivity extends AppCompatActivity {
    HomeAdapter adapter;
    List<Home> transactionList;
    RecyclerView recyclerView;
    String query;
    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        recyclerView = findViewById(R.id.recylcerView);

        Bundle extras = getIntent().getExtras();
        query = extras.getString("QUERY");
        setTitle('"'+query+'"');

        loadTransactions();
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        transactionList = new ArrayList<>();
    }

    private void loadTransactions(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        if (response.equals("No results Found")){
                            showSnackbar(response);
                        }else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    transactionList.add(new Home(
                                            jsonObject.getInt("sale_id"),
                                            jsonObject.getString("date"),
                                            jsonObject.getString("amount"),
                                            jsonObject.getString("customer_name")



                                    ));
                                }
                                adapter = new HomeAdapter(getApplicationContext(), transactionList);
                                recyclerView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                params.put("query", query);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public void showSnackbar(String stringSnackbar){
        snackbar.make(SearchResultsActivity.this.findViewById(android.R.id.content), stringSnackbar.toString(), Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

}
