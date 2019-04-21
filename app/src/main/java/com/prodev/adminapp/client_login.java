package com.prodev.adminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class client_login extends AppCompatActivity {
    TextView imadmin;
    Button done;
    EditText cln_name,cln_pass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        cln_name = (EditText) findViewById(R.id.client_name);
        cln_pass=  (EditText) findViewById(R.id.client_passs);
        done= (Button)findViewById(R.id.client_done);
        imadmin=(TextView)findViewById(R.id.imadmin_client);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cln_pass.getText().toString().trim().equals("") || cln_name.getText().toString().trim().equals(""))
                {
                    Toast.makeText(client_login.this, "oops! you forgot to enter!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    userLogin();
                }
            }
        });

        imadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(client_login.this, admin_login.class));
            }
        });

    }


    private void userLogin(){
        final String username = cln_name.getText().toString().trim();
        final String password = cln_pass.getText().toString().trim();

        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getString("username"),
                                                "jjj"
                                        );
                                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Some Fault is there, Please restart the app!", Toast.LENGTH_LONG).show();
                    }
                }
        ){
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

