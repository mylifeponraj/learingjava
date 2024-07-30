package com.learn;

import com.generic.annotation.Overload;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String world = "World!!";
        System.out.println(STR."Hello \{world}!");
    }

    @Overload public void display(String message) {
        System.out.println(message);
    }
    public void display() {
        display("What is your name?");
    }
}