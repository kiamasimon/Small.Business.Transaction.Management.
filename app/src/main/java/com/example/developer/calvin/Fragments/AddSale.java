package com.example.developer.calvin.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.developer.calvin.Adapters.SalesAdapter;
import com.example.developer.calvin.Models.Product;
import com.example.developer.calvin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.developer.calvin.Constants.Url.LOAD_PRODUCTS;

public class AddSale extends Fragment {
    private List<Product> products;
    private RecyclerView recyclerView;
    private String TAG = "HomeFragment";
    SalesAdapter adapter;

    public static AddSale newInstance() {
        AddSale fragment = new AddSale();
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
        View view = inflater.inflate(R.layout.fragment_add_sale, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvFeaturedProducts);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        loadProducts();
        return view;
    }

    private void loadProducts() {
        products = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOAD_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response+"");
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                products.add(new Product(
                                        jsonObject.getInt("product_id"),
                                        jsonObject.getString("product_name"),
                                        jsonObject.getString("received_date"),
                                        jsonObject.getString("expiry_date"),
                                        jsonObject.getString("number_of_items"),
                                        jsonObject.getString("price_per_commodity"),
                                        jsonObject.getString("selling_price")));
                            }

                            adapter = new SalesAdapter(getActivity(),products);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


}

