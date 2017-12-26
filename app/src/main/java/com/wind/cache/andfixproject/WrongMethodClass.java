package com.wind.cache.andfixproject;

import android.util.Log;

public class WrongMethodClass {
    public int get(int a, int b) {
        Log.e("WrongMethodClass", "you have run the wrong method !!!!");
        return a*b;
    }
}
