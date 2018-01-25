package com.jwkj.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdl.elog.ELog;
import com.jwkj.CommWebView;
import com.jwkj.WebViewCallback;

public class MainActivity extends AppCompatActivity {
    private CommWebView wv_main;
    private ProgressDialog mProgressDialog;
    private TextView tvTitle;
    private LinearLayout fl_full;
    private EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        mProgressDialog = new ProgressDialog(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mProgressDialog.setMessage("玩命加载中");
        fl_full = (LinearLayout) findViewById(R.id.fl_full);
        etUrl = (EditText) findViewById(R.id.et_url);
        wv_main = (CommWebView) findViewById(R.id.wv_main);
        wv_main.setNetErrorConfig(CommWebView.NetErrorConfig.DEFAULT_BUTTON);
//        wv_main.setCurWebUrl("http://upg.cloudlinks.cn/demo/default.htm")
        wv_main.setCurWebUrl("https://www.baidu.com")
                .startCallback(new WebViewCallback() {
                    @Override
                    public void onStart() {
                        ELog.e("开始调用了"+wv_main.getCurWebUrl());
                        mProgressDialog.show();
                    }

                    @Override
                    public void onProgress(int curProgress) {
                        ELog.e(curProgress);
                        if (curProgress > 80) {//加载完成80%以上就可以隐藏了，防止部分网页不能
                            mProgressDialog.dismiss();
                            tvTitle.setText(wv_main.getWebTitle());
                        }
                    }

                    @Override
                    public void onError(int errorCode, String description, String failingUrl) {
                        super.onError(errorCode, description, failingUrl);
                        ELog.e(errorCode + " \t" + description + "\t" + failingUrl);
                    }
                });
    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wv_main.canGoBack()) {
                wv_main.goBack();//返回上一个页面
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        wv_main.onDestroy();
        super.onDestroy();
    }

    public void onGoUrl(View view) {
        String url = etUrl.getText().toString().trim();
        wv_main.setCurWebUrl(url);
        wv_main.refresh();//需要调用刷新
    }
}
