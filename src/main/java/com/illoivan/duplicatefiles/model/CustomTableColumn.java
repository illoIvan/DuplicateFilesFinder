package com.illoivan.duplicatefiles.model;

import com.illoivan.duplicatefiles.viewmodel.CustomFileViewModel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomTableColumn<S,T> extends TableColumn<S, T>{

	public CustomTableColumn (String text, String propertyName) {
		super(text);
        setStyle(getStyle() +" -fx-font-size: 15px;");
		setCellValueFactory(new PropertyValueFactory<>(propertyName));
		setSortable(false);
	}

    @SuppressWarnings("unchecked")
	public void addCheckbox() {
        this.setCellFactory(p -> {
        	CheckBoxTableCell<CustomFileViewModel, Boolean> ctCell = new CheckBoxTableCell<>();
        	ctCell.getStyleClass().add("custom-checkbox");
            ctCell.setSelectedStateCallback(i -> {
                CustomFileViewModel rowItem = (CustomFileViewModel)this.getTableView().getItems().get(i);
                if(rowItem.hasError()) {
                    ctCell.setGraphic(null);
                	return new SimpleBooleanProperty(false);
                }
                return rowItem.getSelected();
            });

            return (TableCell<S, T>) ctCell;
        });
        
    }
	
}
