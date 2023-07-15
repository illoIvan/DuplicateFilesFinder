package com.illoivan.duplicatefiles.controller.view.commons;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomChangeListener implements ChangeListener<Object>{

    private Region region;
    
    public CustomChangeListener(Region region) {
        this.region = region;
    }
    
    @Override
    public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
        if(region instanceof CheckBox chk) {
        	if(newValue instanceof Integer integer) {
        		chk.setSelected(integer > 0);
        	}else {
        		chk.setSelected(!chk.isSelected());
        	}
        }else if(region instanceof Label lbl) {
            lbl.setText(newValue.toString());
        }else if(region instanceof Spinner<?> sp) {
            sp.setDisable((boolean) oldValue);
        }else if(region instanceof VBox vbox) {
            region.setDisable((boolean) oldValue);
        }else if(region instanceof Button btn) {
            btn.setDisable(newValue != null ? Boolean.FALSE : Boolean.TRUE);
        }
    }
}

