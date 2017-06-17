package com.deeplink.typebinder;

import com.squareup.javapoet.MethodSpec;

/**
 * @author HunkDeng
 * @since 2017/6/17
 */
public class BooleanTypeBinder implements TypeBinder {
    @Override
    public void addStatement(MethodSpec.Builder methodSpecBuilder, String paramName, String bindKey) {
        methodSpecBuilder.addStatement("obj.$L = Boolean.valueOf(param.get($S))", paramName, bindKey);
    }
}
