package com.example.developer.calvin.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.example.developer.calvin.Adapters.HomeAdapter;
import com.example.developer.calvin.Models.Home;
import com.example.developer.calvin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.developer.calvin.Constants.Url.ADD_PURCHASE;
import static com.example.developer.calvin.Constants.Url.ADD_TRANSACTION;
import static com.example.developer.calvin.Constants.Url.GET_PURCHASES;
import static com.example.developer.calvin.Constants.Url.GET_TRANSACTIONS;

public class TransactionFragment extends Fragment {
    EditText edCustomerName, edCustomer_Address,edAmount,edRemark,edPhoneNumber;
    Button bn;
    String phoneNumber;
    String amount;
    Daraja daraja;
    ProgressDialog pd;
    Snackbar snackbar;
    public static TransactionFragment newInstance() {
        TransactionFragment fragment = new TransactionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.Transaction);
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        edCustomerName = view.findViewById(R.id.edCustomerName);
        edCustomer_Address = view.findViewById(R.id.edCustomer_Address);
        edAmount = view.findViewById(R.id.edAmount);
        edRemark = view.findViewById(R.id.edRemark);
        bn = view.findViewById(R.id.bn);
        edPhoneNumber = view.findViewById(R.id.edPhoneNumber);
        pd = new ProgressDialog(getActivity());


        daraja = Daraja.with("rA2371JgGgLgGD7FMeeToKckGIOrFbsg", "wMOLEEspd4cOWtNj",
                new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(getActivity().getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(getActivity(), "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(String error) {
                Log.e(getActivity().getClass().getSimpleName(), error);
            }
        });



        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneNumber = edPhoneNumber.getText().toString().trim();
                amount = edAmount.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    edPhoneNumber.setError("Please Provide a Phone Number");
                    return;
                }
                if(TextUtils.isEmpty(amount)){
                    edAmount.setError("Enter Transaction Amount");
                    return;
                }



                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                        amount,
                        "254708374149",
                        "174379",
                        phoneNumber,
                        "http://mycallbackurl.com/checkout.php",
                        "001ABC",
                        "Goods Payment"
                );

                daraja.requestMPESAExpress(lnmExpress,
                        new DarajaListener<LNMResult>() {
                            @Override
                            public void onResult(@NonNull LNMResult lnmResult) {
                                Log.i(getActivity().getClass().getSimpleName(), lnmResult.ResponseDescription);
                            }
                            @Override
                            public void onError(String error) {
                                Log.i(getActivity().getClass().getSimpleName(), error);
                            }
                        }
                );

            }
        });

        return view;
    }

    private void transactionRequest(){
        pd.setMessage("Processing transaction . . .");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, ADD_TRANSACTION,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        pd.hide();
                        if(response.equals("Transaction Processed Successfully")) {
                            showSnackbar(response);
                            edCustomerName.setText("");
                            edCustomer_Address.setText("");
                            edAmount.setText("");
                            edPhoneNumber.setText("");
                            edRemark.setText("");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ErrorResponse", finalResponse);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("customer_name", edCustomerName.getText().toString());
                params.put("customer_address", edCustomer_Address.getText().toString());
                params.put("amount", edAmount.getText().toString());
                params.put("user_id", sharedPreferences.getString("user_id",""));
                params.put("remarks", edRemark.getText().toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


    public void showSnackbar(String stringSnackbar){
        snackbar.make(getActivity().findViewById(android.R.id.content), stringSnackbar.toString(), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

}



