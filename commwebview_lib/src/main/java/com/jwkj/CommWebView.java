package com.jwkj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


/**
 * 通用webview
 * Created by HDL on 2017/6/23.
 */

public class CommWebView extends LinearLayout {
    /**
     * 是否可以返回上一个页面，默认可以返回上一个页面
     */
    private boolean isCanBack = true;
    /**
     * 当前网页的标题
     */
    private String webTitle = "";
    /**
     * 当前url
     */
    private String curWebUrl = "";
    /**
     * 回调器
     */
    private WebViewCallback callback;
    /**
     * 采用addview(webview)的方式添加到线性布局，可以及时销毁webview
     */
    private WebView webview;

    private Context context;
    private boolean isTransparent = false;

    public CommWebView(Context context) {
        this(context, null);
    }

    public CommWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initConfig(context);
    }

    /**
     * 初始化参数配置
     *
     * @param context
     */
    private void initConfig(final Context context) {
        webview = new WebView(context.getApplicationContext());
        transparent();
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);//设置是否支持与js互相调用
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用网络缓存，开启的话容易导致app膨胀导致卡顿
        webview.setWebViewClient(new WebViewClient() {//设置webviewclient,使其不会由第三方浏览器打开新的url

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadWebUrl(url);
                return true;//设置为true才有效哦
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webTitle = view.getTitle();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadWebUrl(context.getResources().getString(R.string.comm_hdl_web_url_default));
                if (callback != null) {
                    callback.onError(errorCode, description, failingUrl);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();//接受证书
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {//监听加载的过程
            private View mCustomView;
            private CustomViewCallback mCustomViewCallback;

            /**
             * 显示自定义视图，无此方法视频不能播放
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                mCustomView = view;
                mCustomViewCallback = callback;
            }

            @Override
            public void onHideCustomView() {
                webview.setVisibility(View.VISIBLE);
                if (mCustomView == null) {
                    return;
                }
                mCustomView.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();
                mCustomView = null;
                super.onHideCustomView();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                curWebUrl = view.getUrl();
                webTitle = view.getTitle();
                if (callback != null) {
                    callback.onProgress(newProgress);
                }
            }

        });
        setVisibility(View.VISIBLE);
        requestFocus();//请求获取焦点，防止view不能打开输入法问题
        requestFocusFromTouch();//请求获取焦点，防止view不能打开输入法问题
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webview.setLayoutParams(params);
        webview.addJavascriptInterface(new JSCallJava(), "NativeObj");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {//3.0以上暂时关闭硬件加速
//            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        /**
         *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
         *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        }
        addView(webview);
    }

    private void transparent() {
        if (isTransparent) {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            setBackgroundColor(0);
            webview.setBackgroundColor(0);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
        transparent();
    }

    /**
     * 销毁当前的页面
     */
    public void onDestroy() {
        if (webview != null) {
            webview.removeAllViews();
            try {
                webview.destroy();
            } catch (Throwable t) {
            }
            webview = null;
        }
        curWebUrl = "";
    }

    /**
     * 开始回调
     *
     * @param callback
     */
    public CommWebView startCallback(WebViewCallback callback) {
        this.callback = callback;
        loadWebUrl(curWebUrl);
        return this;
    }

    /**
     * 判断是否可以返回上一个页面
     *
     * @return
     */
    public boolean isCanBack() {
        return isCanBack;
    }

    /**
     * 设置是否可以返回上一个页面
     *
     * @param canBack
     */
    public CommWebView setCanBack(boolean canBack) {
        isCanBack = canBack;
        return this;
    }

    /**
     * 获取webview
     *
     * @return
     */
    public WebView getWebview() {
        return webview;
    }

    /**
     * 执行js代码
     *
     * @param js
     * @param callback
     */
    public void evaluateJavascript(String js, ValueCallback<String> callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.evaluateJavascript(js, callback);
        }
    }

    public String getCurWebUrl() {
        return curWebUrl;
    }

    /**
     * 设置当前需要加载的url
     *
     * @param curWebUrl
     */
    public CommWebView setCurWebUrl(String curWebUrl) {
        this.curWebUrl = curWebUrl;
        return this;
    }

    public String getWebTitle() {
        return webTitle;
    }

    /**
     * 加载网页
     *
     * @param url
     */
    private CommWebView loadWebUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            curWebUrl = url;//记录当前的url
            if (webview != null) {
                webview.loadUrl(curWebUrl);//webview加载url
            }
        }
        return this;
    }

    /**
     * 判断是否可以返回上一个页面
     *
     * @return
     */
    public boolean canGoBack() {
        return webview.canGoBack();
    }

    /**
     * 返回到上一个页面
     */
    public void goBack() {
        webview.goBack();
    }

    /**
     * 添加js与java互相调用类.
     * <p>
     * SuppressLint("JavascriptInterface") 表示webview的修复漏洞
     *
     * @param mapClazz js方法与java方法映射类
     * @param objName  对象的名字
     */
    @SuppressLint("JavascriptInterface")
    public CommWebView addJavascriptInterface(Object mapClazz, String objName) {
        webview.addJavascriptInterface(mapClazz, objName);
        return this;
    }

    /**
     * 刷新
     */
    public void refresh() {
        loadWebUrl(curWebUrl);
    }


    public class JSCallJava {
        @JavascriptInterface
        public void refreshPager() {
            if (context != null) {
                /**
                 * 4.4以上的webview，需要在子线程中调用js与java互相调用的代码
                 */
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                });
            }
        }
    }
}
