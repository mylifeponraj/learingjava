package com.learn;

import com.generic.annotation.ComparableData;
import com.generic.annotation.ComparableField;

@ComparableData
public class ComparableDataAnnotation {
    @ComparableField public String name;
    @ComparableField public String age;
    
    public static void main(String[] args) {
        ComparableDataAnnotation obj = new ComparableDataAnnotation();
        obj.name = "Ponraj";
        obj.age = "23";

        ComparableDataAnnotation obj1 = new ComparableDataAnnotation();
        obj1.name = "Ponraj";
        obj1.age = "23";
        System.out.println("Data...");
        System.out.println(obj.name);
        ComparableDataAnnotationComparator comparator = new ComparableDataAnnotationComparator();
        System.out.println(comparator.compare(obj, obj1));
    }
}
