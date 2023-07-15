package com.illoivan.duplicatefiles.controller.view.commons;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;

public class CustomEventHandler implements EventHandler<Event>{

    public Control control;
    
    public CustomEventHandler(Control control) {
        this.control = control;
    }

    @Override
    public void handle(Event event) {
        if(control instanceof Button btn) {
            btn.setDisable(!control.isDisable());
        }
    }

    
}
