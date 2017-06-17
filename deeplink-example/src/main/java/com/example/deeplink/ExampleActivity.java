package com.example.deeplink;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.deeplink.BindParam;
import com.deeplink.BuildConfig;
import com.deeplink.CommonRouteHandler;
import com.deeplink.DeepLink;
import com.deeplink.DeepLinkActivity;

/**
 * How to test it?
 *
 * scripts
 * adb shell
 * am start -W -a android.intent.action.VIEW -d "http://www.example.com/home/12?strVal=str&longVal=121" com.example.deeplink
 */
public class ExampleActivity extends AppCompatActivity {
    @BindParam("param1")
    int intVal;
    @BindParam
    String strVal;
    @BindParam
    long longVal;

    // TODO: add annotation configuration
    static {
        DeepLinkActivity.deepLink.addRoute(new CommonRouteHandler("\\/home[\\/.]*\\/(?<param1>\\d+)$", ExampleActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLink.bind(this, getIntent());
        Log.d(BuildConfig.APPLICATION_ID, "intVal == 12 : " + (intVal == 12));
    }
}