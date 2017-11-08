package com.jwkj;

/**
 * webview回调
 * Created by HDL on 2017/6/23.
 */

public abstract class WebViewCallback {
    /**
     * 开始加载的时候调用
     */
    public void onStart() {
    }

    /**
     * 加载进度回调
     *
     * @param curProgress 当前进度值[0,100]
     */
    public void onProgress(int curProgress) {
    }

    /**
     * 加载错误的时候会回调
     *
     * @param errorCode   错误码
     * @param description 描述
     * @param failingUrl  加载失败的url
     */
    public void onError(int errorCode, String description, String failingUrl) {
    }

    /**
     * 当页面加载完成的时候，此方法不靠谱，如果页面有跳转会回调多次
     */
    public void onPageFinished() {
    }
}
