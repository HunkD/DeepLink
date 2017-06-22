package com.deeplink;

import com.deeplink.typebinder.BooleanTypeBinder;
import com.deeplink.typebinder.FloatTypeBinder;
import com.deeplink.typebinder.IntTypeBinder;
import com.deeplink.typebinder.LongTypeBinder;
import com.deeplink.typebinder.ShortTypeBinder;
import com.deeplink.typebinder.StrListTypeBinder;
import com.deeplink.typebinder.StringTypeBinder;
import com.deeplink.typebinder.TypeBinder;
import com.google.common.reflect.TypeToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import static com.google.auto.common.MoreElements.getPackage;

/**
 * @author HunkDeng
 * @since 2017/6/10
 */
class BindingSet {
    private List<VariableElement> variableElementList = new ArrayList<>();
    private String packageName;
    private String className;
    private ClassName bindingClassName;
    private TypeElement typeElement;
    private static Map<String, TypeBinder> typeBinderMap = new HashMap<>();

    static {
        typeBinderMap.put(String.class.getName(), new StringTypeBinder());
        typeBinderMap.put(int.class.getName(), new IntTypeBinder());
        typeBinderMap.put(long.class.getName(), new LongTypeBinder());
        typeBinderMap.put(short.class.getName(), new ShortTypeBinder());
        typeBinderMap.put(float.class.getName(), new FloatTypeBinder());
        typeBinderMap.put(boolean.class.getName(), new BooleanTypeBinder());
        typeBinderMap.put(List.class.getName(), new StrListTypeBinder());
        typeBinderMap.put(String[].class.getName(), new BooleanTypeBinder());
    }

    private Types typeUtils;

    void add(VariableElement annotatedElement) {
        variableElementList.add(annotatedElement);
    }

    JavaFile brewJava() {
        ClassName map = ClassName.get("java.util", "Map");
        ClassName string = ClassName.get("java.lang", "String");
        ClassName urlParam = ClassName.get("com.deeplink", "UrlParam");
        ParameterSpec parameterSpec = ParameterSpec.builder(ParameterizedTypeName.get(map, string, urlParam), "param")
                .build();
        // create method
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("bind")
                .addParameter(ClassName.get(packageName, className), "obj")
                .addParameter(parameterSpec)
                .addModifiers(Modifier.PUBLIC);
        // inject BindParam
        for (VariableElement variableElement : variableElementList) {
            BindParam bindParam = variableElement.getAnnotation(BindParam.class);
            String bindKey = bindParam.value();
            String paramName = variableElement.getSimpleName().toString();
            if (bindKey.trim().equals("")) {
                bindKey = paramName;
            }
            TypeBinder typeBinder = getTypeBinder(variableElement.asType());
            if (typeBinder != null) {
                typeBinder.addStatement(methodSpecBuilder, paramName, bindKey);
            } else {
                System.out.println("can't find typeBinder for " + variableElement.asType().toString());
            }
        }
        // create class **_DeepLinkBinder.java
        TypeSpec typeSpec = TypeSpec.classBuilder(bindingClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpecBuilder.build())
                .build();

        return JavaFile.builder(packageName, typeSpec)
                .build();
    }

    private TypeBinder getTypeBinder(TypeMirror type) {
        return typeBinderMap.get(typeUtils.erasure(type).toString());
    }

    void parse(TypeElement typeElement) {
        this.typeElement = typeElement;
        packageName = getPackage(typeElement).getQualifiedName().toString();
        className = typeElement.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        bindingClassName = ClassName.get(packageName, className + "_DeepLinkBinder");
    }

    public void setTypes(Types typeUtils) {
        this.typeUtils = typeUtils;
    }
}
