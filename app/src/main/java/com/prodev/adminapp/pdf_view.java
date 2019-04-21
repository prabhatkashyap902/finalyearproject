package com.prodev.adminapp;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

public class pdf_view extends AppCompatActivity {
    Button button;
    WebView webView;
ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_pdf_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String url = getIntent().getExtras().getString("pdf");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




        String doc="<iframe src='http://docs.google.com/gview?embedded=true&url="
                +url+"' width='100%' height='100%' style='border: none;'></iframe>";
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setPluginsEnabled(true);
        webView.getSettings().setAllowFileAccess(true);

        pd = new ProgressDialog(pdf_view.this);
        pd.setMessage("Please wait Loading...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadData( doc , "text/html", "UTF-8");



    }




    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String doc="<iframe src='http://docs.google.com/gview?embedded=true&url="
                    +url+"' width='100%' height='100%' style='border: none;'></iframe>";

            view.loadData( doc , "text/html", "UTF-8");

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
