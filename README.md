# CommWebView
通用webview
- 避免API < 17 下的漏洞
- 加载错误时出现默认页面
- 点击默认页面可重新加载（刷新）
- 方便获取当前在加载页面的url和title
- 与js交互不变

## 导入
app/gradle中加入依赖

```java
compile 'com.jwkj:commwebview:v1.0.5'
````

## 用法

### 初始化并加载页面

```java
         wv_main = (CommWebView) findViewById(R.id.wv_main);
         wv_main.setCurWebUrl("https://www.baidu.com1")
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
```

## 销毁
在avtivity销毁的时候需要将webview销毁

```java
 @Override
    protected void onDestroy() {
        wv_main.onDestroy();
        super.onDestroy();
    }
```

效果图

![](https://github.com/huangdali/commwebview/blob/master/com_web.gif)

中文加载失败页面


![](https://github.com/huangdali/commwebview/blob/master/no_net_zh.png)

英文加载失败页面


![](https://github.com/huangdali/commwebview/blob/master/no_net_us.png)

## 版本记录

1.0.5
- 【新增】英文版加载失败页面（带刷新功能）

1.0.4
- 【新增】中文版加载失败页面（带刷新功能）

1.0.4 以前版本未记录