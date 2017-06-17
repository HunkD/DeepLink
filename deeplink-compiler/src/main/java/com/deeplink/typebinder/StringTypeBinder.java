package com.deeplink.typebinder;

import com.squareup.javapoet.MethodSpec;

/**
 * @author HunkDeng
 * @since 2017/6/11
 */
public class StringTypeBinder implements TypeBinder {
    @Override
    public void addStatement(MethodSpec.Builder methodSpecBuilder, String paramName, String bindKey) {
        methodSpecBuilder.addStatement("obj.$L = param.get($S)", paramName, bindKey);
    }
}
