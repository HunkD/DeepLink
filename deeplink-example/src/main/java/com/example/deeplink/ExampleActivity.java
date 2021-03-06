package com.example.deeplink;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.deeplink.BindLink;
import com.deeplink.BindParam;
import com.deeplink.DeepLink;

import java.util.List;

/**
 * How to test it?
 *
 * scripts
 * adb shell
 * am start -W -a android.intent.action.VIEW -d "http://www.example.com/home/12/xx?strVal=str&longVal=121" com.example.deeplink
 */
@BindLink("/home/{param1}/{param2}")
public class ExampleActivity extends AppCompatActivity {
    @BindParam("param1")
    int intVal;
    @BindParam
    String strVal;
    @BindParam
    long longVal;
    @BindParam
    List<String> stringList;
    @BindParam("param2")
    String p2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLink.bind(this, getIntent());
        Log.d(BuildConfig.APPLICATION_ID, "intVal == 12 : " + (intVal == 12));
    }
}
