package com.tech.Education.Cognitive.App_Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.Education.Cognitive.Helper.DownLoadFile;
import com.tech.Education.Cognitive.R;

public class PdfViewActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressDialog;
    Bundle bundle;

    String filename;


    FloatingActionButton btnDownload;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    String pdf_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        webView = (WebView) findViewById(R.id.webView);
        progressDialog = findViewById(R.id.progress);

        progressDialog.bringToFront();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        String newPdfUrl = null;
        if (bundle != null) {


            pdf_url = bundle.getString("pdf_url");
            newPdfUrl=  pdf_url.replaceAll(" ","%20");

            Log.e("PDF",newPdfUrl);
            filename = bundle.getString("file_name");
            getSupportActionBar().setTitle(filename);

        }


        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + newPdfUrl);


        btnDownload = (FloatingActionButton) findViewById(R.id.fab_download);
        btnDownload.bringToFront();
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(pdf_url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressDialog.setVisibility(View.GONE);
            super.onPageFinished(view, pdf_url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            progressDialog.setVisibility(View.VISIBLE);

            super.onPageStarted(view, pdf_url, favicon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadFile() {

        DownLoadFile downLoadFile = new DownLoadFile(filename+".pdf", PdfViewActivity.this);
        downLoadFile.downLoadFile(pdf_url);
    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(PdfViewActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            downloadFile();

        } else {

            ActivityCompat.requestPermissions(PdfViewActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    downloadFile();

                    return;

                } else {

                    Toast.makeText(PdfViewActivity.this, "Permissions Denied", Toast.LENGTH_SHORT).show();
                }

                return;
            }
        }
    }
}