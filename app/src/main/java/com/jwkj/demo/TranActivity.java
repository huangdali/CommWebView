package com.jwkj.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.jwkj.CommWebView;

public class TranActivity extends FragmentActivity{

    private CommWebView webView;
    private String url = "http://:8080/vas/pages/dialogs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tran);
        webView = (CommWebView) findViewById(R.id.cwv_view);
        webView.setTransparent(true);
        webView.setCurWebUrl(url);
        webView.refresh();
    }
    boolean isTransparent=true;
    public void onDialog(View view) {
        isTransparent=!isTransparent;
        webView.setTransparent(isTransparent);
    }
}
