# CommWebView
通用webview
- 避免API < 17 下的漏洞
- 加载错误时出现默认页面
- 点击默认页面可重新加载（刷新）--->两种错误提示页面
- 方便获取当前在加载页面的url和title
- 与js交互不变
- 可设置背景透明

## 导入
app/gradle中加入依赖

```java
compile 'com.jwkj:commwebview:v1.2.3'
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

中文加载失败页面1(默认)


![](https://github.com/huangdali/commwebview/blob/master/no_net_zh.png)

中文加载失败页面2


![](https://github.com/huangdali/commwebview/blob/master/image.png)

英文加载失败页面1（默认）


![](https://github.com/huangdali/commwebview/blob/master/no_net_us.png)

英文加载失败页面2


![](https://github.com/huangdali/commwebview/blob/master/image_us.png)

## 版本记录

v1.2.3 ( [2017.11.15]() )

- 【优化】删除清单文件中的appname，防止部分依赖时app名字被修改

v1.2.2 ( [2017.11.08]() )

- 【新增】onPageFinished回调

v1.2.1 ( [2017.10.25]() )

- 【新增】视频全屏与退出全屏监听

v1.1.4 ( [2017.09.11]() )

- 【新增】网络错误默认页面，通过方法设置setNetErrorConfig(CommWebView.NetErrorConfig.DEFAULT_BUTTON);，默认是DEFAULT_BODY

v1.1.2 ( [2017.09.08]() )

- 【修复】特殊情况抛出空指针

v1.0.9 ( [2017.09.01]() )

- 【修复】中文环境无网络环境时的标题为英文

v1.0.8 ( [2017.08.21]() )

- 【新增】设置是否背景透明方法setTransparent（默认不透明）

v1.0.7 ( [2017.08.04]() )
- 【新增】evaluateJavascript方法
- 【新增】获取原生WebView方法

v1.0.6
- 【修复】部分网页大不开

v1.0.5
- 【新增】英文版加载失败页面（带刷新功能）

v1.0.4
- 【新增】中文版加载失败页面（带刷新功能）

v1.0.4 以前版本未记录