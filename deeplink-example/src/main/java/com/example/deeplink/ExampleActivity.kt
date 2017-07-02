package com.example.deeplink

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.deeplink.BindLink
import com.deeplink.BindParam
import com.deeplink.DeepLink

/**
 * How to test it?

 * scripts
 * adb shell
 * am start -W -a android.intent.action.VIEW -d "http://www.example.com/home/12?strVal=str&longVal=121" com.example.deeplink
 */
@BindLink("\\/home[\\/.]*\\/(?<param1>\\d+)$")
open class ExampleActivity : AppCompatActivity() {
    @BindParam("param1")
    internal var intVal: Int = 0
    @BindParam
    internal var strVal: String? = null
    @BindParam
    internal var longVal: Long = 0
    @BindParam
    internal var stringList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeepLink.bind(this, intent)
        Log.d(BuildConfig.APPLICATION_ID, "intVal == 12 : " + (intVal == 12))
    }
}
