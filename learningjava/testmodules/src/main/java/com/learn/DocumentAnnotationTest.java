package com.learn;

import com.generic.annotation.Document;

@Document(author = "Ponraj Suthanthiramani", version = "1.0", description = "This class is available from begining...")
public class DocumentAnnotationTest {

    @Document(author = "Ponraj Suthanthiramani", version = "1.0", description = "This method is available from begining...")
    public void display() {
        System.out.println("Display method is called...");
    }
}
