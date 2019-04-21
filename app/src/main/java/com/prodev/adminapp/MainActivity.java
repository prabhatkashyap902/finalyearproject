package com.prodev.adminapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity{

    //Declaring views
    private Button SelectButton;
    private Button UploadButton;

    private EditText PdfNameEditText;
    LinearLayout constraint;

    Uri uri;

Pdf pdf;
    ArrayList<String> list = new ArrayList<String>();
    //ListView to show the fetched Pdfs from the server
    ListView listView;

    //button to fetch the intiate the fetching of pdfs.
    Button buttonFetch;

    //Progress bar to check the progress of obtaining pdfs
    ProgressDialog progressDialog;

    //an array to hold the different pdf objects
    ArrayList<Pdf> pdfList= new ArrayList<Pdf>();

    //pdf adapter

    PdfAdapter pdfAdapter;



    public static final String PDF_UPLOAD_HTTP_URL = "http://admin007.coolpage.biz/AndroidPdfUpload/upload.php";
    public static final String PDF_FETCH_URL = "http://admin007.coolpage.biz/AndroidPdfUpload/getPdfs.php";




    public int PDF_REQ_CODE = 1;

    String PdfNameHolder, PdfPathHolder, PdfID;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String mItemSelected;

    ArrayList<String> selecteditems = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        AllowRunTimePermission();

        SelectButton = (Button) findViewById(R.id.button);
        UploadButton = (Button) findViewById(R.id.button2);
        PdfNameEditText = (EditText) findViewById(R.id.editText);
constraint=(LinearLayout) findViewById(R.id.constraint);





        SelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // PDF selection code start from here .

                Intent intent = new Intent();

                intent.setType("application/pdf");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE);

            }
        });

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                //
                if(!PdfNameEditText.getText().toString().trim().equals("")){
                    if(!SelectButton.getText().toString().equals("Select")) {
                        sharepdf();
                        final String str[] = new String[list.size()];

                        // ArrayList to Array Conversion
                        for (int j = 0; j < list.size(); j++) {

                            // Assign each value to String array
                            str[j] = list.get(j);
                        }
                        checkedItems = new boolean[list.size()];

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                        mBuilder.setTitle("Select clients");
                        mBuilder.setMultiChoiceItems(str, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                                if(isChecked){
                                    mUserItems.add(position);
                                }else{
                                    mUserItems.remove((Integer.valueOf(position)));
                                }
                            }
                        });

                        //mBuilder.setCancelable(false);
                        mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                String item = "";

                                for (int i = 0; i < mUserItems.size(); i++) {
                                    selecteditems.add(str[mUserItems.get(i)]);
                                    // item = item + str[mUserItems.get(i)];

                                    if (i != mUserItems.size() - 1) {
                                        item = item + ", ";
                                    }

                                }
                                // mItemSelected.setText(item);
                                //PdfUploadFunction();

                                PdfUploadFunction();
                                Toast.makeText(MainActivity.this, "File is uploading! Kindly refresh after sometime!", Toast.LENGTH_SHORT).show();

                                PdfNameEditText.getText().clear();
                                SelectButton.setText("SELECT");

                                mUserItems.clear();
                                selecteditems.clear();

                            }
                        });

                        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        mBuilder.setNeutralButton("ClearAll", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                for (int i = 0; i < checkedItems.length; i++) {
                                    checkedItems[i] = false;
                                    mUserItems.clear();
                                    //mItemSelected.setText("");
                                }
                            }
                        });

                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Oops! you forgot to select file!", Toast.LENGTH_SHORT).show();
                    }


                }
                else {
                    Toast.makeText(MainActivity.this, "Enter some name for file!", Toast.LENGTH_SHORT).show();
                }



               // PdfUploadFunction();
            }
        });


        //initializing ListView
        listView = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listView);

        //initializing buttonFetch

        //initializing progressDialog

        progressDialog = new ProgressDialog(this);

        //Setting clicklistener
        getPdfs();



        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Pdf pdf = (Pdf) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),pdf_view.class);
                intent.putExtra("pdf",pdf.getUrl());
                startActivity(intent);
                /*
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(pdf.getUrl()));
                startActivity(intent);
*/
            }
        });



/*
        this.listView.setLongClickable(true);
        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                //Do your tasks here


                AlertDialog.Builder alert = new AlertDialog.Builder(
                        MainActivity.this);
                alert.setTitle("Check info!!");
                //alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });

*/








        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pdfList.clear();
                getPdfs();// your code
                pullToRefresh.setRefreshing(false);
            }
        });



    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        TextView textViewurl = (TextView) info.targetView.findViewById(R.id.textViewUrl);
        TextView textViewname = (TextView)info.targetView.findViewById(R.id.textViewName) ;
        final String url = textViewurl.getText().toString();

        switch(item.getItemId()) {
            case R.id.info:
                Intent i  = new Intent(getApplicationContext(),info_by_admin.class);

                long selectid = info.id; //_id from database in this case
                int selectpos = info.position;


               // Toast.makeText(getApplicationContext(), "Selected " + text, Toast.LENGTH_LONG).show();


                i.putExtra("urlname",url);
                i.putExtra("filename",textViewname.getText().toString());
                startActivity(i);
                return true;
            case R.id.delete:


               // Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete this File?");
                alert.setMessage("Are you sure to delete this File?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url2="http://admin007.coolpage.biz/AndroidPdfUpload/delete_pdf.php?url=" +url;


                        RequestQueue re2 = Volley.newRequestQueue(getApplicationContext());
                        final StringRequest sr2 =  new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                Snackbar snackbar = Snackbar
                                        .make(constraint, "Deleted Successfully", Snackbar.LENGTH_LONG);

                                snackbar.show();
                                pdfList.clear();
                                getPdfs();
                               // pdfAdapter.notifyDataSetChanged();//this line


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Toast.makeText(con, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        re2.add(sr2);
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

alert.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            SelectButton.setText("PDF is Selected");
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PdfUploadFunction() {

        PdfNameHolder = PdfNameEditText.getText().toString().trim();

        PdfPathHolder = FilePath.getPath(this, uri);

        if (PdfPathHolder == null) {

            Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show();

        } else {

            try {

    PdfID = UUID.randomUUID().toString();
for(int i=0;i<selecteditems.size();i++){
    new MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
            .addFileToUpload(PdfPathHolder, "pdf")
            .addParameter("name", PdfNameHolder)
            .addParameter("cln_name",selecteditems.get(i))
            .setNotificationConfig(new UploadNotificationConfig())
            .setMaxRetries(5)
            .startUpload();
}
            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            Toast.makeText(MainActivity.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }






    private void getPdfs() {

        progressDialog.setMessage("Fetching Pdfs... Please Wait");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PDF_FETCH_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(MainActivity.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray = obj.getJSONArray("pdfs");

                            for(int i=0;i<jsonArray.length();i++){

                                //Declaring a json object corresponding to every pdf object in our json Array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //Declaring a Pdf object to add it to the ArrayList  pdfList
                                Pdf pdf  = new Pdf();
                                String pdfName = jsonObject.getString("name");
                                String pdfUrl = jsonObject.getString("url");
                                pdf.setName(pdfName);
                                pdf.setUrl(pdfUrl);
                                pdfList.add(pdf);

                            }

                            pdfAdapter=new PdfAdapter(MainActivity.this,R.layout.item_in_listview, pdfList);

                            listView.setAdapter(pdfAdapter);

                            pdfAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

    }

private void sharepdf(){
    String url = "http://admin007.coolpage.biz/login/items/get_username.php";



    RequestQueue rq = Volley.newRequestQueue(this);
    JsonArrayRequest jar = new JsonArrayRequest(Request.Method.GET,url,null,
            new Response.Listener<JSONArray> (){

                @Override
                public void onResponse(JSONArray response) {

                    try{list.clear();
                        // Loop through the array elements
                        for(int i=0;i<response.length();i++){
                            // Get current json object
                            list.add(response.getJSONObject(i).getString("username"));

                        }



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.createclient:
                Intent i=new Intent(MainActivity.this,client_signup.class);
                startActivity(i);
                break;
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, Login_1.class));
                break;
            case R.id.exitid:
                finishAffinity ();
                break;
        }
        return true;
    }



}
