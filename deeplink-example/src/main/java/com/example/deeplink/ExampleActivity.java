package com.example.deeplink;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.deeplink.*;
import com.deeplink.BuildConfig;

import java.util.HashMap;

/**
 * @author HunkDeng
 * @since 2017/6/17
 */
public class ExampleActivity extends AppCompatActivity {
    @BindParam("param1")
    int intVal;
    @BindParam
    String strVal;
    @BindParam
    long longVal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: test code block
        Intent intent = new Intent();
        HashMap<String, String> params = new HashMap<>();
        params.put("param1", "12");
        params.put("strVal", "222zz0");
        params.put("longVal", "20");
        intent.putExtra(DeepLink.DEEP_LINK_PARAM, params);
        intent.putExtra(DeepLink.FROM_DEEP_LINK, true);

        DeepLink.bind(this, intent);

        Log.d(BuildConfig.APPLICATION_ID, "intVal == 12 : " + (intVal == 12));
    }
}
