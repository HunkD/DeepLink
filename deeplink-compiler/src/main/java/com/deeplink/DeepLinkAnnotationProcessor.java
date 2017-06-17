package com.deeplink;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author HunkDeng
 * @since 2017/6/4
 */
@AutoService(Processor.class)
public class DeepLinkAnnotationProcessor extends AbstractProcessor {
    private ProcessingEnvironment env;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.env = processingEnvironment;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<TypeElement, BindingSet> bindingMap = new HashMap<>();
        // remapping class to variables
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(BindParam.class)) {
            Element owner = annotatedElement.getEnclosingElement();
            if (annotatedElement instanceof VariableElement
                    && owner instanceof TypeElement) {
                BindingSet bindingSet;
                if (bindingMap.containsKey(owner)) {
                    bindingSet = bindingMap.get(owner);
                } else {
                    bindingSet = new BindingSet();
                    bindingMap.put((TypeElement) owner, bindingSet);
                }
                bindingSet.add((VariableElement)annotatedElement);
            }
        }
        // generate java file
        for (Map.Entry<TypeElement, BindingSet> entry : bindingMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            BindingSet bindingSet = entry.getValue();
            // build
            bindingSet.parse(typeElement);
            JavaFile javaFile = bindingSet.brewJava();
            // write java file
            try {
                javaFile.writeTo(env.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindParam.class);
        return annotations;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
