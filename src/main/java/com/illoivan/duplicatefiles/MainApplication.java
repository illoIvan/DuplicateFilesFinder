package com.illoivan.duplicatefiles;

/*
    Actual MainApplication.
    required for proper jar compilation. Otherwise the error would come out
    error comes from sun.launcher.LauncherHelper in the java.base module. 
    The reason for this is that the Main app extends Application 
    and has a main method. If that is the case, the LauncherHelper will check 
    for the javafx.graphics module to be present as a named module. 
    If that module is not present, the launch is aborted.
    
    Since the Java launcher checks if the main class extends javafx.application.Application, 
    and in that case it requires the JavaFX runtime available as modules (not as jars), a 
    possible workaround to make it work, should be adding a new Main class that will be the 
    main class of your project, and that class will be the one that calls your JavaFX Application class.
*/
public class MainApplication {
    public static void main(String[] args) {
        MainApplicationJavaFx.main(args);
    }    
}
