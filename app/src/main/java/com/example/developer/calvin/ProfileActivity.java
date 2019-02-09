package com.example.developer.calvin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.GET_PROFILE;
import static com.example.developer.calvin.Constants.Url.GET_TRANSACTION_DETAILS;
import static com.example.developer.calvin.Constants.Url.UPDATE_PROFILE;
import static com.example.developer.calvin.Constants.Url.UPDATE_TRANSACTION_;

public class ProfileActivity extends AppCompatActivity {
    EditText edUserName,edEmail,edPassword;
    Button bnUpdate;
    String name,email,password;
    Snackbar  snackbar;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        edUserName = findViewById(R.id.edUserName);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        bnUpdate = findViewById(R.id.bnUpdate);

        loadProfile();

        bnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


    }

    private void loadProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn", "" + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                name = jsonObject.getString("user_name");
                                email = jsonObject.getString("email");
                                password = jsonObject.getString("password");
                            }
                            edUserName.setText(name);
                            edEmail.setText(email);
                            edPassword.setText(password);
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
                SharedPreferences sharedPreferences  = getSharedPreferences("MyPref", MODE_PRIVATE);
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sharedPreferences.getString("user_id",""));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public void updateProfile(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("conn",""+response);
                        if (response.equals("Profile successfully updated.")){
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
                params.put("user_id", sharedPreferences.getString("user_id",""));
                params.put("user_name", edUserName.getText().toString().trim());
                params.put("email", edEmail.getText().toString().trim());
                params.put("password", edPassword.getText().toString().trim());
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
