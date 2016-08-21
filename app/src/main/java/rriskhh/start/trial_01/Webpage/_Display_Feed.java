package rriskhh.start.trial_01.Webpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import rriskhh.start.trial_01.R;

public class _Display_Feed extends AppCompatActivity {

    private ProgressDialog pDialog;
    private WebView webview;
    private String url;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_feed);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        url = i.getStringExtra("page_url");


        webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        /*pDialog = new ProgressDialog(
                DisplayRSSItem.this);
        pDialog.setMessage("Loading..");
        pDialog.setIndeterminate(false);
        //pDialog.setCancelable(false);
        pDialog.show();*/
        pDialog = ProgressDialog.show(this,"Webpage","Loading..");
        if(url != null){
            webview.loadUrl(url);
            webview.setWebViewClient(new DisPlayWebPageActivityClient());
        }
    }


    private class DisPlayWebPageActivityClient extends WebViewClient {

        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view, url, favicon);

        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url){
            super.onPageFinished(view, url);
            pDialog.dismiss();
        }
    }

}
