package com.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public interface RouteHandler {
    Intent handle(Uri uri, Context context);
}
