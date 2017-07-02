# DeepLink
Let's use annotation bind parameter in deeplink path and query. This library is target to make binding parameter in deeplink easier for you. 
1. Using @BindLink to bind the deeplink path, which also let you catch the parameter in deeplink url's path.
2. Using @BindParam to bind the field with parameter in deeplink url even in path.

## How to use it in your project?
Add dependency in your gradle file
  
    compile 'com.deeplink:deeplink-annotation:1.0.1'
    compile 'com.deeplink:deeplink-resolver:1.0.1'
    annotationProcessor 'com.deeplink:deeplink-compiler:1.0.1'
    
Add handler activity to AndroidManifest.xml which will receive the associated link intent

    <activity android:name="com.deeplink.DeepLinkActivity"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:exported="true"
            android:screenOrientation="portrait">
            <intent-filter
                android:label="@string/app_name">
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <!--https-->
                <data
                    android:host="*.example.com"
                    android:pathPrefix="/"
                    android:scheme="https" />
             </intent-filter>
    </activity>

Add @BindLink on your page class which need to handle the deeplink url, currently we are using regular expression to define the path which need to be handle.
Add annotation on your field which need to bind
   
    @BindLink("\\/home[\\/.]*\\/(?<param1>\\d+)$")
    open class ExampleActivity : AppCompatActivity() {
       @BindParam("param1")
       internal var intVal: Int = 0
       @BindParam
       internal var strVal: String? = null
       ...
    }
    
The most important: call Deeplink to auto binding those fields for you!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeepLink.bind(this, intent)
    }
