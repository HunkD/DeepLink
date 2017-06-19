package com.deeplink;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by HunkDeng on 2017/6/18.
 */
@Retention(CLASS) @Target(TYPE)
public @interface BindLink {
    /**
     * @return link which is bind to this type
     */
    String[] value();
}
