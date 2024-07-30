package com.generic.annotation.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.generic.annotation.Overload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OverloadAnnotation implements GenericAnnotation {

    @Override public Boolean process(RoundEnvironment roundEnv, Types types, Elements elementUtils, Filer filer, Messager messager, Set<? extends TypeElement> annotations, ProcessingEnvironment processingEnv) {
        Map<String, Boolean> overloadedMethods = new HashMap<>();

        //Step 1: Iterate thru the Class and get all the methods annotated with @Overload
        for(Element element : roundEnv.getElementsAnnotatedWith(Overload.class)){
            overloadedMethods.put(element.getSimpleName().toString(), Boolean.FALSE);
        }
        log.debug("All Overloaded Methods [] ", overloadedMethods);
        return Boolean.FALSE;
    }

}
