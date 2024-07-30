package com.generic.annotation.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.generic.annotation.Overload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OverloadAnnotation implements GenericAnnotation {
    private String validateMethod = "";
    @Override public Boolean process(RoundEnvironment roundEnv, Types types, Elements elementUtils, Filer filer, Messager messager, Set<? extends TypeElement> annotations, ProcessingEnvironment processingEnv) {
        Map<String, Boolean> overloadedMethods = new HashMap<>();
        String annotedClass = "";
        //Step 1: Iterate thru the Class and get all the methods annotated with @Overload
        for(Element element : roundEnv.getElementsAnnotatedWith(Overload.class)){
            System.out.println("Method: "+element.getSimpleName().toString());
            overloadedMethods.put(element.getSimpleName().toString(), Boolean.FALSE);
        }


        for(Element element : roundEnv.getElementsAnnotatedWith(Overload.class)){
            List<String> methodNames = new ArrayList<>();
            //Get all the Class Element
            annotedClass = element.getEnclosingElement().toString();
            if(element.getEnclosingElement().getKind() == ElementKind.CLASS){
                //Step 1: Iterate all thru to the parent Object class to get all methods in each object
                TypeElement classElement = (TypeElement) element.getEnclosingElement();
                Element superClass = null;
                
                while(superClass == null || !superClass.toString().contains("java.lang.Object")) {
                    TypeMirror mirror = null;
                    if(superClass == null) mirror = classElement.getSuperclass();
                    else mirror =  ((TypeElement)superClass).getSuperclass();
                    DeclaredType dType = (DeclaredType) mirror;
                    superClass = dType.asElement();
                    superClass.getEnclosedElements().stream().filter(t -> t.getKind() == ElementKind.METHOD).forEach(method -> {
                        methodNames.add(method.getSimpleName().toString());
                    });
                }


                //Step 2: Iterate thru to get methods in the annotate class and validate if it is overloaded properly
                validateMethod = element.getSimpleName().toString();
                element.getEnclosingElement().getEnclosedElements().stream().filter(t -> t.getKind() == ElementKind.METHOD).forEach(method -> {
                    if(methodNames.contains(method.getSimpleName().toString())) {
                        overloadedMethods.put(method.getSimpleName().toString(), Boolean.TRUE);
                    }
                    methodNames.add(method.getSimpleName().toString());
                });

                //Step 3: If the method name is not matching then thru compilation exception
                if(!overloadedMethods.get(validateMethod)) {
                    messager.printMessage(javax.tools.Diagnostic.Kind.ERROR, "The method with name {"+validateMethod+"} in the class {"+annotedClass+"} is not overloading properly. Please check if the method is overloaded properly.", element);
                }
            }
        }
        return Boolean.FALSE;
    }

}
