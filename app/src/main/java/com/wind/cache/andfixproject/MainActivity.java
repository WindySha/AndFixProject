package com.wind.cache.andfixproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.sample_text);
        WrongMethodClass wrong = new WrongMethodClass();
        Object result = wrong.get(10, 10);  //修復不成功的話，結果应该100，修復成功的話，結果是10020
        tv.setText(result+"");
    }
}
