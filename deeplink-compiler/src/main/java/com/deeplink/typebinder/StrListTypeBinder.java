package com.deeplink.typebinder;

import com.squareup.javapoet.MethodSpec;

/**
 * @author HunkDeng
 * @since 2017/6/19
 */
public class StrListTypeBinder implements TypeBinder {
    @Override
    public void addStatement(MethodSpec.Builder methodSpecBuilder, String paramName, String bindKey) {
        methodSpecBuilder.beginControlFlow("if (param.get($S) != null) ", bindKey);
        methodSpecBuilder.addStatement("obj.$L = param.get($S).list", paramName, bindKey);
        methodSpecBuilder.endControlFlow();
    }
}
