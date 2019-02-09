package com.example.developer.calvin.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.developer.calvin.Adapters.HomeAdapter;
import com.example.developer.calvin.Adapters.PurchaseAdapter;
import com.example.developer.calvin.Models.Home;
import com.example.developer.calvin.Models.Purchase;
import com.example.developer.calvin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.developer.calvin.Constants.Url.GET_PURCHASES;
import static com.example.developer.calvin.Constants.Url.GET_TRANSACTIONS;

public class HomeFragment extends Fragment {
    HomeAdapter adapter;
    PurchaseAdapter adapter1;
    List<Home> transactionList;
    List<Purchase> purchaseList;
    RecyclerView recyclerView,recyclerView1;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.home_title);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView1 = view.findViewById(R.id.recylcerView1);
        recyclerView = view.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(false);
        recyclerView1.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionList = new ArrayList<>();
        purchaseList = new ArrayList<>();

        loadTransactions();
        loadPurchases();
        return view;
    }

    private void loadTransactions(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TRANSACTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                transactionList.add(new Home(
                                        jsonObject.getInt("sale_id"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("amount"),
                                        jsonObject.getString("customer_name")

                                ));
                            }
                            adapter = new HomeAdapter(getActivity(),transactionList);
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
                sharedPreferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sharedPreferences.getString("user_id",""));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void loadPurchases() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PURCHASES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn", "" + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                purchaseList.add(new Purchase(
                                        jsonObject.getInt("product_id"),
                                        jsonObject.getString("product_name"),
                                        jsonObject.getString("received_date"),
                                        jsonObject.getString("total"),
                                        jsonObject.getString("selling_price")

                                ));
                            }
                            adapter1 = new PurchaseAdapter(getActivity(), purchaseList);
                            recyclerView1.setAdapter(adapter1);
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
                SharedPreferences sharedPreferences;
                sharedPreferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sharedPreferences.getString("user_id", ""));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
    }



