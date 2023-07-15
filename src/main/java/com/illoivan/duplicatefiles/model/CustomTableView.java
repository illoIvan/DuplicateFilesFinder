package com.illoivan.duplicatefiles.model;

import java.util.ArrayList;
import java.util.List;

import com.illoivan.duplicatefiles.viewmodel.CustomFileDuplicateViewModel;
import com.illoivan.duplicatefiles.viewmodel.CustomFileViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class CustomTableView extends TableView<CustomFileViewModel> {
    
	private ObservableList<CustomFileDuplicateViewModel> listDuplicate;
	private ObservableList<CustomFileViewModel> customFiles;
	private List<CustomTableColumn<CustomFileViewModel, ?>> columns = new ArrayList<>();
	
	public CustomTableView() {
        super();
    }
	
	public void assingsColorByList(String colorOne, String colorTwo) {
		if(listDuplicate.isEmpty()) return;

		String colorUp = colorOne.isBlank() ? "-fx-background-color: white;": colorOne;
		String colorDown = colorTwo.isBlank() ? "-fx-background-color: #DADADA;": colorTwo;
		String errorColor = "-fx-background-color: #FF6600;";
        boolean changeColor = false;
        for (CustomFileDuplicateViewModel customFileDuplicateViewModel : listDuplicate) {
            for (CustomFileViewModel cf : customFileDuplicateViewModel.getDuplicate()) {
                if(changeColor) {
                    cf.setCss(colorUp);
                }else {
                    cf.setCss(colorDown);
                }
                if(cf.hasError()) {
                	cf.setCss(errorColor);
                }
                customFiles.add(cf);
            }
            changeColor = !changeColor;
        }

	}
	
	public void setTableItems(ObservableList<CustomFileDuplicateViewModel> listDuplicate) {
		this.listDuplicate = listDuplicate;
		this.customFiles = FXCollections.observableArrayList();
		setItems(customFiles);
	}
	
	public void addColumn(CustomTableColumn<CustomFileViewModel, ?> column) {
        columns.add(column);
        getColumns().add(column);
    }

    public void removeColumn(CustomTableColumn<CustomFileViewModel, ?> column) {
        columns.remove(column);
        getColumns().remove(column);
    }

    public List<CustomTableColumn<CustomFileViewModel, ?>> getTableColumns() {
        return this.columns;
    }

    public void removeCustomFiles(ObservableList<CustomFileViewModel> listCustomFiles) {
    	customFiles.removeAll(listCustomFiles);
    }

}

