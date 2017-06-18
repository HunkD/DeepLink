package com.deeplink;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.google.auto.common.MoreElements.getPackage;

/**
 * Created by HunkDeng on 2017/6/18.
 */
public class BindingLinkSet {
    private List<TypeElement> elements = new ArrayList<>();
    public static final String javaClassName = "DeepLinkConfig";
    public static final String packageName = "com.deeplink";

    public void add(TypeElement typeElement) {
        elements.add(typeElement);
    }

    public JavaFile brewJava() {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        for (TypeElement typeElement : elements) {
            BindLink bindLink = typeElement.getAnnotation(BindLink.class);
            String packageName = getPackage(typeElement).getQualifiedName().toString();
            String className = typeElement.getQualifiedName().toString().substring(
                    packageName.length() + 1).replace('.', '$');
            for (String url : bindLink.value()) {
                codeBlockBuilder.addStatement(
                        "DeepLink.addRoute(new CommonRouteHandler($S, $L.class))",
                        url, packageName + "." + className);
            }
        }
        // create class **_DeepLinkBinder.java
        ClassName className = ClassName.get(packageName, javaClassName);
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addStaticBlock(codeBlockBuilder.build())
                .build();
        return JavaFile.builder(packageName, typeSpec)
                .build();
    }
}
