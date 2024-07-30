package com.generic.annotation.processor;

import javax.lang.model.element.Modifier;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import com.generic.annotation.ComparableData;
import com.generic.annotation.ComparableField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class ComparableDataAnnotation implements GenericAnnotation {

    @Override public Boolean process(RoundEnvironment roundEnv, Types types, Elements elementUtils, Filer filer, Messager messager, Set<? extends TypeElement> annotations, ProcessingEnvironment processingEnv) {
        for( Element element : roundEnv.getElementsAnnotatedWith(ComparableData.class)) {
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            System.out.println("Element Package "+packageName);
            System.out.println("Class Name: "+element.getSimpleName().toString());

            //Step 1: Get the Class Template
            TypeSpec.Builder comparatorTypeSpec = getClassDescription(element);
            comparatorTypeSpec.addMethod(getBodyOfTheClass(element, roundEnv).build());

            JavaFile file = JavaFile.builder(packageName, comparatorTypeSpec.build()).build();
            try {
                file.writeTo(filer);
            }
            catch (IOException ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
            }
        }
        return Boolean.FALSE;
    }
    private MethodSpec.Builder getBodyOfTheClass(Element classElement, RoundEnvironment roundEnv) {
        CodeBlock.Builder compareBuilder = CodeBlock.builder();

        ParameterSpec lhsParameterSpec = ParameterSpec.builder(ClassName.get(classElement.asType()), LHS).build();
        ParameterSpec rhsParameterSpec = ParameterSpec.builder(ClassName.get(classElement.asType()), RHS).build();

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(COMPARE)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(lhsParameterSpec)
            .addParameter(rhsParameterSpec)
            .returns(int.class);
        String compareGT = "";
        String compareLT = "";
        Boolean addAdditionalComare = Boolean.FALSE;
        ComparableField annotation = null;
        for( Element element : roundEnv.getElementsAnnotatedWith(ComparableField.class)) {
            annotation = element.getAnnotation(ComparableField.class);
            if (element.asType().getKind().isPrimitive()) {
                if(!addAdditionalComare) {
                    compareGT += String.format("%s.%s > %s.%s ", LHS, element.getSimpleName(), RHS, element.getSimpleName());
                    compareLT += String.format("%s.%s < %s.%s ", LHS, element.getSimpleName(), RHS, element.getSimpleName());
                }
                else {
                    compareGT += String.format("&& %s.%s > %s.%s ", LHS, element.getSimpleName(), RHS, element.getSimpleName());
                    compareLT += String.format("&& %s.%s < %s.%s ", LHS, element.getSimpleName(), RHS, element.getSimpleName());
                }
            } else {
                if(!addAdditionalComare) {
                    compareGT += String.format("%s.%s.compareTo(%s.%s) >= 0 ", LHS, element.getSimpleName(), RHS, element.getSimpleName());
                    compareLT += String.format("%s.%s.compareTo(%s.%s) < 0 ", LHS, element.getSimpleName(), RHS, element.getSimpleName());
                }
                else {
                    compareGT += String.format("&& %s.%s.compareTo(%s.%s) >= 0", LHS, element.getSimpleName(), RHS, element.getSimpleName());
                    compareLT += String.format("&& %s.%s.compareTo(%s.%s) < 0", LHS, element.getSimpleName(), RHS, element.getSimpleName());
                }
            }
            addAdditionalComare = Boolean.TRUE;
        }
        compareBuilder = compareBuilder.beginControlFlow("if ("+compareGT+") ")
                .addStatement("return $L", isPositive(annotation) ? "1" : "-1")
                .endControlFlow()
                .beginControlFlow("if ("+compareLT+") ")
                .addStatement("return $L", isPositive(annotation) ? "-1" : "1")
                .endControlFlow();
        compareBuilder.addStatement("return 0");
        methodSpecBuilder.addCode(compareBuilder.build());
        return methodSpecBuilder;
    }
    private boolean isPositive(ComparableField f) {
        return f.greaterThan() == ComparableField.GreaterThan.IS_POSITIVE;
    }
    private TypeSpec.Builder getClassDescription (Element classElement) {
        String className = String.format(NAME_FORMAT, classElement.getSimpleName());
        TypeSpec.Builder comparatorTypeSpec = TypeSpec.classBuilder(className)
                                                        .addModifiers(Modifier.PUBLIC)
                                                        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Comparator.class), TypeName.get(classElement.asType())));
        return comparatorTypeSpec;
    }

    private static final String COMPARE = "compare";
    private static final String NAME_FORMAT = "%sComparator";
    private static final String RHS = "rhs";
    private static final String LHS = "lhs";
}
