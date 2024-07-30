package com.generic.annotation.processor;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.google.auto.service.AutoService;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.generic.annotation.Overload")
@SupportedSourceVersion(SourceVersion.RELEASE_22)
public class GenericAnnotationProcessor extends AbstractProcessor{

    @Override public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        //Important Utility Objects
        messager = processingEnv.getMessager();
        filler = processingEnv.getFiler();
        options = processingEnv.getOptions();
        elements = processingEnv.getElementUtils();
        types = processingEnv.getTypeUtils();
    }
    
    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        GenericAnnotation genericOverloadAnnotation = new OverloadAnnotation();
        genericOverloadAnnotation.process(roundEnv, types, elements, filler, messager, annotations, processingEnv);

        GenericAnnotation genericComparableAnnotation = new ComparableDataAnnotation();
        genericComparableAnnotation.process(roundEnv, types, elements, filler, messager, annotations, processingEnv);
        
        return Boolean.FALSE;
    }

    private Types types;
    private Elements elements;
    private Map<String,String> options;
    private Filer filler;
    private Messager messager;
}
