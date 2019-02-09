package com.example.developer.calvin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;

public class MainActivity extends AppCompatActivity {
    Daraja daraja;
    String phoneNumber, amount;
    Bundle bundle;
    TextView Amount, PhoneNumber;
    Button bn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MPesa Payment");
        Amount = findViewById(R.id.amount);
        PhoneNumber = findViewById(R.id.phonenumber);
        bn = findViewById(R.id.bn);

        bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phone_number");
        amount = bundle.getString("amount");

        Amount.setText(amount);
        PhoneNumber.setText(phoneNumber);

        daraja = Daraja.with("rA2371JgGgLgGD7FMeeToKckGIOrFbsg", "wMOLEEspd4cOWtNj",
                new DarajaListener<AccessToken>() {
                    @Override
                    public void onResult(@NonNull AccessToken accessToken) {
                        Log.i(MainActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
                        Toast.makeText(MainActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(String error) {
                        Log.e(MainActivity.this.getClass().getSimpleName(), error);
                    }
                });

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mpesa();
                Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
                startActivity(intent);
            }
        });
    }

    public void Mpesa(){

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(getApplicationContext(),"No phone number provided",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(amount)){
            Toast.makeText(getApplicationContext(),"Invalid Amount",Toast.LENGTH_SHORT).show();
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
                        Log.i(MainActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                    }
                    @Override
                    public void onError(String error) {
                        Log.i(MainActivity.this.getClass().getSimpleName(), error);
                    }
                }
        );
    }
}
