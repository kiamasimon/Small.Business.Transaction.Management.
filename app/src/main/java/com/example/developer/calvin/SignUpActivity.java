package com.example.developer.calvin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.example.developer.calvin.Constants.Url.SIGNUP_URL;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpEmail,signUpName,signUpPassword,signUpPhoneNumber;
    Button signupButton;
    CheckBox checkBoxTerms;private Snackbar snackbar;   private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pd = new ProgressDialog(SignUpActivity.this);
        signupButton =(Button)findViewById(R.id.signupButton);
        signUpEmail = (EditText)findViewById(R.id.signUpEmail);
        signUpName =(EditText)findViewById(R.id.signUpName);
        signUpPassword = (EditText)findViewById(R.id.signUpPassword);
        checkBoxTerms = (CheckBox)findViewById(R.id.checkBoxTerms);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxTerms.isChecked() == true){

                    signupRequest();

                }else{

                    Toast.makeText(getApplicationContext(),"Please Accept Terms & Services",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void signupRequest(){
        pd.setMessage("Signing Up . . .");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, SIGNUP_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        pd.hide();
                        //Response
                        showSnackbar(response);

                        if(response.equals("Successfully Signed In")) {

                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("ErrorResponse", finalResponse);
                        showSnackbar("Network Error");


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email", signUpEmail.getText().toString());
                params.put("password", signUpPassword.getText().toString());
                params.put("user_name", signUpName.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public void showSnackbar(String stringSnackbar){
        snackbar.make(findViewById(android.R.id.content), stringSnackbar.toString(), Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }
}
