package com.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Keep
public class CommonRouteHandler implements RouteHandler {
    private static final String ENCODING_UTF_8 = "utf-8";
    private String regex;
    private Class<?> clazz;
    private HashMap<String, String> customParam = new HashMap<>();

    public CommonRouteHandler(String regex, Class<?> clazz) {
        this.regex = regex;
        this.clazz = clazz;
    }

    @Override
    public Intent handle(Uri uri, Context context) {
        HashMap<String, UrlParam> params = new HashMap<>();
        String path = uri.getPath();
        // We will use name expression to extract the parameter in path, but it won't work in following case:
        // url : /r/ddd/31303/?p=1
        // regex : \/r\/.+\/(?<pid>\d+)$
        // So remove the last char '/' in path to make it works
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        // When there's no path segments, redirect user to home page.
        if (path.equals("") && regex.equals("")) {
            params.putAll(convert(customParam));
            Intent intent = new Intent(context, clazz);
            intent.putExtra(DeepLink.FROM_DEEP_LINK, true);
            intent.putExtra(DeepLink.DEEP_LINK_PARAM, params);
            return intent;
        }
        //
        if (path.matches(regex)) {
            // get parameter in path segments
            Pattern p = Pattern.compile(regex);
            final Matcher m = p.matcher(path);
            m.find();
            Map<String, String> namedGroups = m.namedGroups();
            params.putAll(convert(namedGroups));
            // get parameter in query
            Set<String> queryParameterNames = uri.getQueryParameterNames();
            for (String name : queryParameterNames) {
                List<String> list = uri.getQueryParameters(name);
                UrlParam urlParam;
                if (list.size() > 1) {
                    urlParam = UrlParam.from(list);
                } else {
                    urlParam = UrlParam.from(list.get(0));
                }
                params.put(name, urlParam);
            }
            // put all custom parameter into final parameter map
            params.putAll(convert(customParam));
            // build intent to pass whole params
            Intent intent = new Intent(context, clazz);
            intent.putExtra(DeepLink.FROM_DEEP_LINK, true);
            intent.putExtra(DeepLink.DEEP_LINK_PARAM, params);
            return intent;
        } else {
            return null;
        }
    }

    private Map<String, UrlParam> convert(@NonNull Map<String, String> strMap) {
        HashMap<String, UrlParam> map = new HashMap<>(strMap.size());
        for (Map.Entry<String, String> entry : strMap.entrySet()) {
            map.put(entry.getKey(), UrlParam.from(entry.getValue()));
        }
        return map;
    }

    public CommonRouteHandler addCustomParam(String key, String value) {
        customParam.put(key, value);
        return this;
    }

    @NonNull
    public static Map<String, String> getQueryStringMap(String urlString) {
        Map<String, String> queryPairs = new HashMap<>();
        if (!TextUtils.isEmpty(urlString)) {
            Uri uri = Uri.parse(urlString);
            if (uri != null) {
                for (String pair : uri.getQueryParameterNames()) {
                    try {
                        queryPairs.put(
                                URLDecoder.decode(pair, ENCODING_UTF_8),
                                URLDecoder.decode(uri.getQueryParameter(pair), ENCODING_UTF_8));
                    } catch (UnsupportedEncodingException ex) {
                        Log.e(CommonRouteHandler.class.getSimpleName(), ex.getMessage());
                    }
                }
            }
        }
        return queryPairs;
    }
}
