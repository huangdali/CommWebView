package com.jwkj.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hdl.elog.ELog;
import com.jwkj.CommWebView;
import com.jwkj.WebViewCallback;

public class MainActivity extends AppCompatActivity {
    private CommWebView wv_main;
    private ProgressDialog mProgressDialog;
    private TextView tvTitle;
    private FrameLayout fl_full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialog = new ProgressDialog(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mProgressDialog.setMessage("玩命加载中");
        fl_full= (FrameLayout) findViewById(R.id.fl_full);
        wv_main = (CommWebView) findViewById(R.id.wv_main);
        wv_main.setCurWebUrl("http://upg.cloudlinks.cn/demo/default.htm")
                .addJavascriptInterface(new JSCallJava(), "NativeObj")
                .startCallback(new WebViewCallback() {
                    @Override
                    public void onStart() {
                        ELog.e("开始调用了");
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

    public class JSCallJava {
        @JavascriptInterface
        public void refresh() {
            ELog.e("点击刷新了");
            /**
             * 4.4以上的webview，需要在子线程中调用js与java互相调用的代码
             */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wv_main.refresh();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        wv_main.onDestroy();
        super.onDestroy();
    }
}
