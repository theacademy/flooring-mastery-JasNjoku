package com.floormastery;

import com.floormastery.controller.FloorMasteryController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    public static void main(String[] args) {
        Logger.getLogger("org.springframework").setLevel(Level.WARNING); //Hide startup Message

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.scan("com.floormastery");
        applicationContext.refresh();

        FloorMasteryController controller = applicationContext.getBean("floorMasteryController", FloorMasteryController.class);
        controller.run();

    }
}