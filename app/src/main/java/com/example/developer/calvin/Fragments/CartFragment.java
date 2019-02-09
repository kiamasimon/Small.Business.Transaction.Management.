package com.example.developer.calvin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.developer.calvin.Adapters.CartRecyclerViewAdapter;
import com.example.developer.calvin.Models.Cart;
import com.example.developer.calvin.ProfileActivity;
import com.example.developer.calvin.R;
import com.example.developer.calvin.SalesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.GET_CART;

public class CartFragment extends Fragment {
    private List<Cart> cartList;
    private RecyclerView rvCart;
    private String TAG = "CartFragment";
    private CartRecyclerViewAdapter adapter;
    private TextView tvTotal, tvPlaceOrder;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private double total;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Cart");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCart = (RecyclerView) view.findViewById(R.id.rvCart);
        tvTotal = (TextView) view.findViewById(R.id.tvInTotal);
        tvPlaceOrder = (TextView) view.findViewById(R.id.tvPlaceOrder);

        initData();
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rvCart.setHasFixedSize(false);
        rvCart.setLayoutManager(llm);
        rvCart.setAdapter(adapter);

        tvPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartList.isEmpty()){

                }else {
                    Intent intent = new Intent(getContext(), SalesActivity.class);
                    intent.putExtra("TOTAL", total);
                    String quantity = "";
                    for (int i = 0; i < cartList.size(); i++){
                        Cart c = cartList.get(i);
                        if(i == cartList.size()-1){
                            quantity += c.getOrderedQuantity();
                        }else{
                            quantity += c.getOrderedQuantity()+";";
                        }
                    }
                    intent.putExtra("QUANTITY", quantity);
                    getContext().startActivity(intent);
                }
            }
        });

        return view;
    }

    public void evaluateTotal() {
        total = 0;
        for(Cart c : cartList){
            total += c.getProductPrice()*c.getOrderedQuantity();
        }
        tvTotal.setText(df.format(total));
    }

    public void initData(){
        cartList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                cartList.add(new Cart(jsonObject1.getInt("cart_id"),
                                        jsonObject1.getInt("user_id"),
                                        jsonObject1.getInt("product_id"),
                                        jsonObject1.getString("product_name"),
                                        jsonObject1.getDouble("selling_price"),
                                        jsonObject1.getString("received_date"),
                                        jsonObject1.getInt("number_of_items"),1));
                            }
                            adapter = new CartRecyclerViewAdapter(getActivity(),CartFragment.this,cartList);
                            rvCart.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                params.put("user_id", sharedPreferences.getString("user_id",""));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }



    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
        evaluateTotal();
    }
}
