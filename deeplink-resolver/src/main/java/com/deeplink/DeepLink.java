package com.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HunkDeng
 * @since 2017/6/4
 */
public class DeepLink {

    public static final String FROM_DEEP_LINK = "FROM_DEEP_LINK";
    public static final String DEEP_LINK_PARAM = "DEEP_LINK_PARAM";
    private static Map<Class<?>, Object> BINDINGS = new HashMap<>();
    private static boolean INITED;

    public static void bind(@NonNull Object object, @Nullable Intent intent) {
        if (intent == null
                || intent.getExtras() == null
                || !intent.getBooleanExtra(FROM_DEEP_LINK, false)
                || intent.getSerializableExtra(DEEP_LINK_PARAM) == null) {
            return;
        }
        Map<String, String> paramMap = (Map<String, String>) intent.getSerializableExtra(DEEP_LINK_PARAM);

        Class<?> targetClass = object.getClass();
        Object deepLinkBinder = BINDINGS.get(targetClass);

        if (deepLinkBinder == null) {
            String deepLinkBinderClassName = targetClass.getName() + "_DeepLinkBinder";
            try {
                Class<?> binderClass = targetClass.getClassLoader().loadClass(deepLinkBinderClassName);
                deepLinkBinder = binderClass.newInstance();
                BINDINGS.put(targetClass, deepLinkBinder);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Do you forgot to annotate your parameter? there's no binder class generated.");
            }
        }

        // if deepLinkBinder still null
        if (deepLinkBinder != null) {
            ReflectionHelpers.callInstanceMethod(
                    deepLinkBinder,
                    "bind",
                    ReflectionHelpers.ClassParameter.from(object.getClass(), object),
                    ReflectionHelpers.ClassParameter.from(Map.class, paramMap));
        }
    }

    public Intent resolveUrl(@Nullable Uri uri, Context context) {
        if (uri != null && !TextUtils.isEmpty(uri.toString())) {
            for (RouteHandler handler : routeHandlers) {
                Intent intent = handler.handle(uri, context);
                if (intent != null) {
                    return intent;
                }
            }
        }
        return null;
    }

    private static ArrayList<RouteHandler> routeHandlers = new ArrayList<>();

    public static void addRoute(RouteHandler routeHandler) {
        routeHandlers.add(routeHandler);
    }

    public static synchronized void initIfNeed() {
        if (INITED) {
            return;
        }
        try {
            Object deepLinkConfig = Class.forName("com.deeplink.DeepLinkConfig").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        INITED = true;
    }
}