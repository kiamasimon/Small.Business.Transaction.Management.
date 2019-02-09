package com.example.developer.calvin.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
import com.example.developer.calvin.MainActivity;
import com.example.developer.calvin.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.ADD_PRODUCTS_URL;

public class PurchaseFragment extends Fragment {
   EditText  etProduct, etQuantity, etRate, etTotal,etProfit,sellingPrice;
   ProgressDialog pd; Snackbar snackbar; Button bn,dExpiry,dRecieved; DatePickerDialog.OnDateSetListener Received,Expiry; DatePickerDialog datePickerDialog;
   String ReceivedDate,ExpiryDate;
   EditText edPhoneNumber;
   String textResult;
   String rv;

    public static PurchaseFragment newInstance() {
        PurchaseFragment fragment = new PurchaseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.Purchase);
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        sellingPrice = view.findViewById(R.id.sellingPrice); etProduct = view.findViewById(R.id.etProduct);
        etQuantity = view.findViewById(R.id.etQuantity); etRate = view.findViewById(R.id.etRate); etTotal = view.findViewById(R.id.etTotal);
        etProfit = (EditText) view.findViewById(R.id.etProfit);
        bn = (Button) view.findViewById(R.id.bn); dExpiry = view.findViewById(R.id.dExpiry);
        dRecieved = view.findViewById(R.id.dReceived); pd = new ProgressDialog(getActivity());
        edPhoneNumber = view.findViewById(R.id.edPhoneNumber);


        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupRequest();

            }
        });

        dRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });
        dExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate2();
            }
        });

        Date date = new Date();
        Received = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                datePicker.setCalendarViewShown(true);

                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                String date = day+"/"+month+"/"+year;
                java.util.Date dte = calendar.getTime();
                ReceivedDate = date;
                Log.i("conn",""+ReceivedDate);
            }
        };

        Expiry = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                datePicker.setCalendarViewShown(true);

                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                String date = day+"/"+month+"/"+year;
                java.util.Date dte = calendar.getTime();
                ExpiryDate = date;
                Log.i("conn",""+ExpiryDate);
            }
        };

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        };
        etQuantity.addTextChangedListener(textWatcher);
        etRate.addTextChangedListener(textWatcher);

        TextWatcher Watcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                calculate1();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        etProfit.addTextChangedListener(Watcher);
        return view;
    }


    private void calculate() {
        double a = 0;
        double b = 1;
        double result;

        if (etQuantity != null)
            a = Double.parseDouble(!etQuantity.getText().toString().equals("")? etQuantity.getText().toString():"0");
        if (etRate != null)
            b = Double.parseDouble(!etRate.getText().toString().equals("")? etRate.getText().toString():"1");
        result = a * b;
        rv = String.valueOf(result);
        etTotal.setText(rv +"Ksh");
    }


    private void calculate1() {
        double buyingprice = 0;
        double profit = 0.0;
        double sellingprice;

        if (etRate != null)
            buyingprice = Double.parseDouble(!etRate.getText().toString().equals("")? etRate.getText().toString():"0");
        if (etProfit != null)
            profit = Double.parseDouble(!etProfit.getText().toString().equals("")? etProfit.getText().toString():"0");
        sellingprice = ((100 + profit) * buyingprice)/100;
        textResult = String.valueOf(sellingprice);
        sellingPrice.setText(textResult+"Ksh");
    }


    public void chooseDate(){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), Received, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        datePickerDialog.show();
    }


    public void chooseDate2(){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), Expiry, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        datePickerDialog.show();
    }


    private void signupRequest(){
        pd.setMessage("Adding Purchase Details . . .");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, ADD_PRODUCTS_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);

                        pd.hide();
                        if(response.equals("Product Submitted Successfully")) {
                            showSnackbar(response);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("amount", etTotal.getText().toString().trim());
                            intent.putExtra("phone_number",edPhoneNumber.getText().toString().trim());
                            startActivity(intent);
                            etProfit.setText("");
                            sellingPrice.setText("");
                            etProduct.setText("");
                            etQuantity.setText("");
                            etRate.setText("");
                            etTotal.setText("");
                        }else if (response.equals("Error Submitting")){
                            showSnackbar(response);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ErrorResponse", response);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
                Map<String, String>  params = new HashMap<String, String>();
                Log.i("conn",""+ sellingPrice.getText().toString());
                params.put("product_name", etProduct.getText().toString());
                params.put("received_date", ReceivedDate);
                params.put("expiry_date", ExpiryDate);
                params.put("user_id", sharedPreferences.getString("user_id",""));
                params.put("quantity", etQuantity.getText().toString());
                params.put("rate", etRate.getText().toString());
                params.put("total", etTotal.getText().toString());
                params.put("profit", etProfit.getText().toString());
                params.put("selling_price", textResult);
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