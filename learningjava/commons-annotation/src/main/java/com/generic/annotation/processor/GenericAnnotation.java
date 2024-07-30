package com.generic.annotation.processor;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public interface GenericAnnotation {
    public Boolean process(RoundEnvironment roundEnv, Types types, Elements elementUtils, Filer filer, Messager messager, Set<? extends TypeElement> annotations, ProcessingEnvironment processingEnv);
}
