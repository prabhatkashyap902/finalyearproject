package com.prodev.adminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Login_1 extends AppCompatActivity {
    Button adm_btn,cln_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);

        adm_btn =  (Button)findViewById(R.id.admin_btn);
        cln_btn=(Button)findViewById(R.id.client_btn);


        if(SharedPrefManager.getInstance(this).isLoggedIn()){

            if(!SharedPrefManager.getInstance(getApplicationContext()).getUsername().equals("Admin")){
                finish();
                Intent i  = new Intent(Login_1.this, Main2Activity.class);
                startActivity(i);

            }
            else{
                finish();
                Intent j = new Intent(Login_1.this, MainActivity.class);
                startActivity(j);

            }
        }


        adm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(Login_1.this,admin_login.class);
                startActivity(i);

            }
        });
        cln_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i =new Intent(Login_1.this,client_login.class);
                startActivity(i);
            }
        });


    }
}
