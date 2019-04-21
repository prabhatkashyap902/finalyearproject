package com.prodev.adminapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                finish();
                Intent i  = new Intent(Splash_screen.this, Login_1.class);
                startActivity(i);


               /* if(!SharedPrefManager.getInstance(getApplicationContext()).getUsername().equals("Admin")){
                    Intent i  = new Intent(Splash_screen.this, Main2Activity.class);
                    startActivity(i);

                }
                else{
                    Intent j = new Intent(Splash_screen.this, MainActivity.class);
                    startActivity(j);

                }*/
                // This method will be executed once the timer is over

            }
        }, 3000);

    }
}
