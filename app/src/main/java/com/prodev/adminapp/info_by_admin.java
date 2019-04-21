package com.prodev.adminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class info_by_admin extends AppCompatActivity {
    TextView txname,txshared,txseen,txlink;
    String cln;
    String l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_by_admin);

        txname = (TextView)findViewById(R.id.filenameid);
        txseen = (TextView)findViewById(R.id.seenbyid);
        txshared = (TextView)findViewById(R.id.sharedwithid);
        txlink =(TextView)findViewById(R.id.linkid);

seenby();

       // Toast.makeText(this, ""+getIntent().getExtras().getString("url"), Toast.LENGTH_SHORT).show();
        txname.setText(getIntent().getExtras().getString("filename"));
        txlink.setText(getIntent().getExtras().getString("urlname"));



    }


    public void seenby(){
        String url = "http://admin007.coolpage.biz/AndroidPdfUpload/get_url.php?url="+getIntent().getExtras().getString("urlname");



        RequestQueue rq = Volley.newRequestQueue(this);
        JsonArrayRequest jar = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray> (){

                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                             cln=response.getJSONObject(0).getString("cln_name");
                           //Toast.makeText(info_by_admin.this, ""+cln, Toast.LENGTH_SHORT).show();

                            // Loop through the array elements


txshared.setText(cln);

                            //Toast.makeText(MainActivity.this, ""+list.size(), Toast.LENGTH_SHORT).show();


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                //Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show();
            }

        });

        rq.add(jar);

    }
}
