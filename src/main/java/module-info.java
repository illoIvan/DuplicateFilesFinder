module duplicatefiles {

    exports com.illoivan.duplicatefiles.exception;
    exports com.illoivan.duplicatefiles.repository.search;
    exports com.illoivan.duplicatefiles.service;
    exports com.illoivan.duplicatefiles.controller;
    exports com.illoivan.duplicatefiles.repository;
    exports com.illoivan.duplicatefiles.utils;
    exports com.illoivan.duplicatefiles;
    exports com.illoivan.duplicatefiles.viewmodel;
    exports com.illoivan.duplicatefiles.model;
    opens com.illoivan.duplicatefiles.controller.view to javafx.fxml;
    exports com.illoivan.duplicatefiles.controller.view;
    exports com.illoivan.duplicatefiles.controller.view.commons;
    
    requires java.xml;
    requires transitive javafx.controls;
    requires javafx.base;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires org.apache.tika.core;
    requires org.apache.tika.parser.audiovideo;
    requires lombok;
    requires java.desktop;
	requires org.apache.logging.log4j;
}
