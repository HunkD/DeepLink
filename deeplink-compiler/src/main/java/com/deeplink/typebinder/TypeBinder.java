package com.deeplink.typebinder;

import com.squareup.javapoet.MethodSpec;

/**
 * @author HunkDeng
 * @since 2017/6/11
 */
public interface TypeBinder {
    void addStatement(MethodSpec.Builder methodSpecBuilder, String paramName, String bindKey);
}
