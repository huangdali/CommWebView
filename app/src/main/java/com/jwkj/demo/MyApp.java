package com.jwkj.demo;

import android.app.Application;

import com.hdl.CrashExceptioner;

/**
 * Created by HDL on 2017/9/26.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashExceptioner.init(this);//最好放在onCreate方法的最后
        //CrashExceptioner.setShowErrorDetails(false);//设置不显示详细错误按钮，默认为true
    }
}
