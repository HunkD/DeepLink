package com.deeplink.typebinder;

import com.squareup.javapoet.MethodSpec;

/**
 * @author HunkDeng
 * @since 2017/6/17
 */
public class ShortTypeBinder implements TypeBinder {
    @Override
    public void addStatement(MethodSpec.Builder methodSpecBuilder, String paramName, String bindKey) {
        methodSpecBuilder.beginControlFlow("if (param.get($S) != null) ", bindKey);
        methodSpecBuilder.addStatement("obj.$L = Short.valueOf(param.get($S).first)", paramName, bindKey);
        methodSpecBuilder.endControlFlow();
    }
}
