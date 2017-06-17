package com.deeplink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeepLinkActivity extends Activity {

    public static DeepLink deepLink = new DeepLink();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = getIntent().getData();
        if (uri != null) {
        }
        // find target page
        Intent intent = deepLink.resolveUrl(uri, this);
        if (intent != null) {
            startActivity(intent);
        } else {
            if (uri != null) {
                openUrlInBrowser(this, uri.toString());
            }
        }
        finish();
    }

    public static void openUrlInBrowser(Context context, String url) {
        List<Intent> webIntents = new ArrayList<Intent>();

        Uri uri = Uri.parse(url);
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent marketIntent = intent;

        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                Intent tryIntent = new Intent(Intent.ACTION_VIEW, uri);

                if (!info.activityInfo.packageName.startsWith(getRuntimePackageName(context))) {
                    tryIntent.setPackage(info.activityInfo.packageName);
                    webIntents.add(tryIntent);
                }
            }

            if (webIntents.size() <= 0) {
                return;
            }
            Intent chooserIntent = Intent.createChooser(webIntents.remove(0), "Browser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    webIntents.toArray(new Parcelable[]{}));

            marketIntent = chooserIntent;
        }

        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        if (marketIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(marketIntent);
        }
    }

    private static String getRuntimePackageName(Context context) {
        return null;
    }
}
