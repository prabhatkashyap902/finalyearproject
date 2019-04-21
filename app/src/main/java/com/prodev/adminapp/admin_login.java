package com.prodev.adminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class admin_login extends AppCompatActivity {
    EditText pass;
    TextView imclient;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        pass  =  (EditText)findViewById(R.id.pass_admin);
        imclient = (TextView)findViewById(R.id.imclient_admin);
        login=(Button)findViewById(R.id.admin_login);






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().trim().equals("12345")){
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin("Admin",pass.getText().toString().trim());
                    finish();
                    Intent i = new Intent(admin_login.this,MainActivity.class);
                    startActivity(i);

                }
                else{
                    Toast.makeText(admin_login.this, "Sorry, Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i= new Intent(admin_login.this,client_login.class);
                startActivity(i);
            }
        });

    }
}
