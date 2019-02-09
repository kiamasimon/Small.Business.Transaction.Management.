package com.example.developer.calvin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.developer.calvin.Constants.ErrorMessageDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static com.example.developer.calvin.Constants.Url.PLACE_ORDER;

public class SalesActivity extends AppCompatActivity{
    private ActionBar actionBar;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private double total;
    private String quantity;
    TextView tvInTotal,tvOrder;
    EditText ed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_sales);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvInTotal = findViewById(R.id.tvInTotal);
        tvOrder = findViewById(R.id.tvOrder);
        ed = findViewById(R.id.name);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            total = extras.getDouble("TOTAL");
            quantity = extras.getString("QUANTITY");
            tvInTotal.setText(df.format(total));
            Log.i("conn",""+total+quantity);
            tvOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeOrder();
                }
            });
        }

    }

    private void placeOrder() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);

        try {
            String url = "http://192.168.137.1/Calvin/User/place_order.php";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", sharedPreferences.getString("user_id",""));
            jsonBody.put("customer_name", ed.getText().toString().trim());
            jsonBody.put("amount", total);
            jsonBody.put("quantity", quantity);

            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, PLACE_ORDER, jsonBody,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("conn",""+response);
                    try {
                        int status = response.getInt("status");
                        if(status == 0){
                            String message = response.getString("message");
                            showError(message);
                        }else{
                            String message = response.getString("message");
                            showSuccess(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showError(getString(R.string.general_error));
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(jor);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    private void showError(String errorMessage) {
        ErrorMessageDialog.showMessage(getString(R.string.error), errorMessage, this);
    }

    private void showSuccess(String successMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.success);
        builder.setMessage(successMessage);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(SalesActivity.this, DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
