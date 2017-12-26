package com.wind.cache.andfixproject;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        AndFixManager.startAndFix(this);  //AndFix进行热修复
//        AndFixManager.startHotFix(this);  //整体ArtMethod替换
//        AndFixManager.startFixByJava(this); // Java方法来进行热修复
        super.onCreate();
    }


}
