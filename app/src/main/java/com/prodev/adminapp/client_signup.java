package com.prodev.adminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class client_signup extends AppCompatActivity {
    Button register;
    EditText name, pass, pass2;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signup);

        name = (EditText) findViewById(R.id.lonameet);
        pass = (EditText) findViewById(R.id.passwordet);
        pass2 = (EditText)findViewById(R.id.passwordet2);
        register = (Button) findViewById(R.id.registerbtn);
        progressDialog = new ProgressDialog(this);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().equals("")|| pass.getText().toString().equals("")|| pass2.getText().toString().equals("")
                        ){
                    Toast.makeText(client_signup.this, "Oops! you forgot to enter the details!", Toast.LENGTH_SHORT).show();
                }
                else{



                    if(pass2.getText().toString().equals(pass.getText().toString())){


                        registerUser();

                    }
                    else{
                        Toast.makeText(client_signup.this, "Password Doesn't match!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


    }


    public void registerUser() {
        final String username = name.getText().toString().trim();
        final String password = pass.getText().toString().trim();


        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            // Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                            Toast.makeText(client_signup.this, "Client Account Created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(client_signup.this,MainActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), " Connect to Internet!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }





}
