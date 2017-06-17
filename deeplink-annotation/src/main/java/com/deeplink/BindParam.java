package com.deeplink;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @author HunkDeng
 * @since 2017/6/4
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindParam {
    /**
     * @return parameter name which will use to bind it with target field.
     */
    String value() default "";
}
